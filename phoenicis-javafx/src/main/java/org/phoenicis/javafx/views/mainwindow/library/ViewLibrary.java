/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.javafx.views.mainwindow.library;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewLibrary extends MainWindowView<LibrarySideBar> {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewLibrary.class);

    private LibrarySideBar sideBar;

    private MiniatureListWidget<ShortcutDTO> applicationListWidget;

    private TabPane libraryTabs;
    private Runnable onTabOpened = () -> {};

    private Consumer<ShortcutDTO> onShortcutSelected = shortcut -> {};
    private Consumer<ShortcutDTO> onShortcutDoubleClicked = shortcut -> {};

    public ViewLibrary(String applicationName, ThemeManager themeManager) {
        super("Library", themeManager);
        this.getStyleClass().add("mainWindowScene");

        this.sideBar = new LibrarySideBar(applicationName);

        this.drawContent();

        this.setSideBar(sideBar);
        this.showRightView(libraryTabs);
    }

    public void setOnShortcutSelected(Consumer<ShortcutDTO> onShortcutSelected) {
        this.onShortcutSelected = onShortcutSelected;
    }

    public void setOnShortcutDoubleClicked(Consumer<ShortcutDTO> onShortcutDoubleClicked) {
        this.onShortcutDoubleClicked = onShortcutDoubleClicked;
    }

    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.sideBar.setOnShortcutStop(onShortcutStop);
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.sideBar.setOnSearch(onSearch);
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.sideBar.setOnShortcutRun(onShortcutRun);
    }

    public void populate(List<ShortcutDTO> shortcutDTOs) {
        applicationListWidget.setItems(shortcutDTOs);

        applicationListWidget.setOnMouseClicked(event -> {
            sideBar.hideShortcut();
            applicationListWidget.unselectAll();
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

        applicationListWidget = MiniatureListWidget.create(MiniatureListWidget.Element::create, (selectedItem, event) -> {
            ShortcutDTO shortcutDTO = selectedItem.getValue();

            applicationListWidget.unselectAll();
            applicationListWidget.select(selectedItem);
            onShortcutSelected.accept(shortcutDTO);

            sideBar.showShortcut(shortcutDTO);

            if(event.getClickCount() == 2) {
                onShortcutDoubleClicked.accept(shortcutDTO);
            }

            event.consume();
        });

        installedApplication.setContent(applicationListWidget);
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
        this.sideBar.setOnOpenConsole(onOpenConsole);
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.sideBar.setOnShortcutUninstall(onShortcutUninstall);
    }

    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.sideBar.setOnScriptRun(onScriptRun);
    }
}
