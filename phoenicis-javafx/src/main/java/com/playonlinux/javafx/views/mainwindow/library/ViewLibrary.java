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

import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import com.playonlinux.javafx.views.mainwindow.ui.LeftBarTitle;
import com.playonlinux.javafx.views.mainwindow.ui.LeftButton;
import com.playonlinux.javafx.views.mainwindow.ui.LeftSpacer;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ViewLibrary extends MainWindowView {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewLibrary.class);

    private final String applicationName;
    private LeftButton runScript;
    private LeftButton runConsole;
    private ApplicationListWidget applicationListWidget;
    private TextField searchBar;
    private TabPane libraryTabs;
    private Runnable onTabOpened = () -> {};

    public ViewLibrary(String applicationName) {
        super();
        this.getStyleClass().add("mainWindowScene");

        this.applicationName = applicationName;

        this.drawSideBar();
        this.drawContent();

        showRightView(libraryTabs);
    }

    private void drawContent() {
        libraryTabs = new TabPane();
        libraryTabs.getStyleClass().add("rightPane");

        final Tab installedApplication = new Tab();
        installedApplication.setClosable(false);
        installedApplication.setText(translate("My applications"));
        libraryTabs.getTabs().add(installedApplication);

        applicationListWidget = new ApplicationListWidget();
        applicationListWidget.getStyleClass().add("rightPane");

        installedApplication.setContent(applicationListWidget);
    }

    @Override
    protected void drawSideBar() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> applyFilter(searchBar.getText()));

        this.runScript = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/script.png",
                translate("Run a script"));
        this.runConsole = new LeftButton("/com/playonlinux/javafx/views/mainwindow/library/console.png",
                translate(applicationName + " console"));

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Advanced tools"), runScript, runConsole);

        super.drawSideBar();
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
}
