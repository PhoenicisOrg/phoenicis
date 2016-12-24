/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.javafx.views.mainwindow.library;

import com.phoenicis.library.dto.ShortcutDTO;
import com.playonlinux.javafx.views.common.widget.MiniatureListWidget;
import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import com.playonlinux.javafx.views.mainwindow.ui.LeftBarTitle;
import com.playonlinux.javafx.views.mainwindow.ui.LeftButton;
import com.playonlinux.javafx.views.mainwindow.ui.LeftButtonGroup;
import com.playonlinux.javafx.views.mainwindow.ui.LeftSpacer;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ViewLibrary extends MainWindowView {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewLibrary.class);

    private final String applicationName;
    private LeftButton runScript;
    private LeftButton runConsole;
    private MiniatureListWidget applicationListWidget;
    private TextField searchBar;
    private TabPane libraryTabs;
    private Runnable onTabOpened = () -> {};
    private Consumer<ShortcutDTO> onShortcutSelected = shortcut -> {};
    private Consumer<ShortcutDTO> onShortcutDoubleClicked = shortcut -> {};

    private Consumer<ShortcutDTO> onShortcutRun = shortcut -> {};
    private Consumer<ShortcutDTO> onShortcutUninstall = shortcutDTO -> {};

    public ViewLibrary(String applicationName) {
        super();
        this.getStyleClass().add("mainWindowScene");

        this.applicationName = applicationName;

        this.drawSideBar();
        this.drawContent();

        showRightView(libraryTabs);
    }

    public void setOnShortcutSelected(Consumer<ShortcutDTO> onShortcutSelected) {
        this.onShortcutSelected = onShortcutSelected;
    }

    public void setOnShortcutDoubleClicked(Consumer<ShortcutDTO> onShortcutDoubleClicked) {
        this.onShortcutDoubleClicked = onShortcutDoubleClicked;
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.onShortcutRun = onShortcutRun;
    }

    public void populate(List<ShortcutDTO> shortcutDTOs) {
        applicationListWidget.clear();
        for (ShortcutDTO shortcutDTO : shortcutDTOs) {
            final MiniatureListWidget.Element selectedItem
                    = applicationListWidget.addItem(shortcutDTO.getName(), shortcutDTO.getMiniature());

            selectedItem.setOnMouseClicked(event -> {
                applicationListWidget.unSelecteAll();
                applicationListWidget.select(selectedItem);
                onShortcutSelected.accept(shortcutDTO);

                clearSideBar();
                drawSideBarWithShortcut(shortcutDTO);

                if(event.getClickCount() == 2) {
                    onShortcutDoubleClicked.accept(shortcutDTO);
                }

                event.consume();
            });
        }

        applicationListWidget.setOnMouseClicked(event -> {
            clearSideBar();
            drawSideBarWithoutShortcut();
            applicationListWidget.unSelecteAll();
            onShortcutSelected.accept(null);
            event.consume();
        });
    }

    private void drawContent() {
        libraryTabs = new TabPane();
        libraryTabs.getStyleClass().add("rightPane");

        final Tab installedApplication = new Tab();
        installedApplication.setClosable(false);
        installedApplication.setText(translate("My applications"));
        libraryTabs.getTabs().add(installedApplication);

        applicationListWidget = MiniatureListWidget.create();

        installedApplication.setContent(applicationListWidget);
    }

    @Override
    protected void drawSideBar() {
        drawSideBarWithoutShortcut();
        super.drawSideBar();
    }

    private void drawSideBarWithShortcut(ShortcutDTO shortcut) {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> applyFilter(searchBar.getText()));

        this.runScript = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/script.png",
                translate("Run a script"));
        this.runConsole = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/console.png",
                translate(applicationName + " console"));

        addToSideBar(searchBar, new LeftSpacer(), shortcutGroup(shortcut), new LeftSpacer(), new LeftBarTitle("Advanced tools"), runScript, runConsole);
    }

    private LeftButtonGroup shortcutGroup(ShortcutDTO shortcut) {
        final LeftButtonGroup shortcutGroup = new LeftButtonGroup(shortcut.getName());
        final LeftButton runButton = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/play.png", translate("Run"));
        final LeftButton uninstallButton = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/remove.png", translate("Uninstall"));

        runButton.setOnMouseClicked(event -> onShortcutRun.accept(shortcut));
        uninstallButton.setOnMouseClicked(event -> onShortcutUninstall.accept(shortcut));
        shortcutGroup.setButtons(Arrays.asList(
                runButton,
                uninstallButton
        ));
        return shortcutGroup;
    }

    private void drawSideBarWithoutShortcut() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> applyFilter(searchBar.getText()));

        this.runScript = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/script.png",
                translate("Run a script"));
        this.runConsole = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/console.png",
                translate(applicationName + " console"));

        addToSideBar(searchBar, new LeftSpacer(), new LeftBarTitle("Advanced tools"), runScript, runConsole);
    }

    private void applyFilter(String searchText) {
        //libraryItems.filter(new LibraryFilter(searchText));
    }

    public void setUpEvents() {
        //libraryItems.setOnChange(this::update);

        runScript.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");
            File scriptToRun = fileChooser.showOpenDialog(null);

            if (scriptToRun != null) {

            }
        });

    }

    public void createNewTab(Tab tab) {
        libraryTabs.getTabs().add(tab);
        libraryTabs.getSelectionModel().select(tab);
        onTabOpened.run();
    }

    public void closeTab(Tab tab) {
        libraryTabs.getTabs().remove(tab);
    }

    public void setOnTabOpened(Runnable onTabOpened) {
        this.onTabOpened = onTabOpened;
    }

    public void setOnOpenConsole(Runnable onOpenConsole) {
        runConsole.setOnMouseClicked(event -> onOpenConsole.run());
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.onShortcutUninstall = onShortcutUninstall;
    }
}
