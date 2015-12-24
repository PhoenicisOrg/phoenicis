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

package com.playonlinux.javafx.mainwindow.library;

import static com.playonlinux.core.lang.Localisation.translate;

import java.io.File;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.python.CommandInterpreterException;
import com.playonlinux.javafx.mainwindow.LeftBarTitle;
import com.playonlinux.javafx.mainwindow.LeftButton;
import com.playonlinux.javafx.mainwindow.LeftSpacer;
import com.playonlinux.javafx.mainwindow.MainWindow;
import com.playonlinux.javafx.mainwindow.MainWindowView;
import com.playonlinux.javafx.mainwindow.console.ConsoleTab;
import com.playonlinux.library.LibraryFilter;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.CommandLineInterpreterFactory;
import com.playonlinux.ui.api.EntitiesProvider;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ViewLibrary extends MainWindowView {

    private final CommandLineInterpreterFactory jythonInterpreterFactory;
    private LeftButton runScript;
    private LeftButton runConsole;
    private static final Logger LOGGER = Logger.getLogger(ViewLibrary.class);
    private ApplicationListWidget applicationListWidget;
    private final EventHandlerLibrary eventHandlerLibrary;
    private TextField searchBar;

    private final EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> libraryItems;
    private TabPane libraryTabs;

    public ViewLibrary(MainWindow parent) {
        super(parent);
        this.getStyleClass().add("mainWindowScene");

        eventHandlerLibrary = new EventHandlerLibrary();
        libraryItems = eventHandlerLibrary.getInstalledApplications();
        jythonInterpreterFactory = eventHandlerLibrary.getJythonInterpreterFactory();

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

        applicationListWidget = new ApplicationListWidget(this);
        applicationListWidget.getStyleClass().add("rightPane");

        installedApplication.setContent(applicationListWidget);
    }

    @Override
    protected void drawSideBar() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> applyFilter(searchBar.getText()));

        this.runScript = new LeftButton("/com/playonlinux/javafx/mainwindow/library/script.png",
                translate("Run a script"));
        this.runConsole = new LeftButton("/com/playonlinux/javafx/mainwindow/library/console.png",
                translate("${application.name} console"));

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Advanced tools"), runScript, runConsole);

        super.drawSideBar();
    }

    private void applyFilter(String searchText) {
        libraryItems.filter(new LibraryFilter(searchText));
    }

    public void setUpEvents() {
        libraryItems.setOnChange(this::update);

        runScript.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");
            File scriptToRun = fileChooser.showOpenDialog(parent);

            try {
                if (scriptToRun != null) {
                    eventHandlerLibrary.runLocalScript(scriptToRun);
                }
            } catch (PlayOnLinuxException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to run the script."));
                alert.setContentText("The file was not found");
                LOGGER.warn("Error while trying to run the script", e);
            }
        });

        runConsole.setOnMouseClicked(event -> runConsole());
    }

    private void runConsole() {
        try {
            createNewTab(new ConsoleTab(jythonInterpreterFactory));
        } catch (CommandInterpreterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to run the console."));
            alert.setContentText("The error was: " + ExceptionUtils.getFullStackTrace(e));
            LOGGER.warn("Error while trying to run the console", e);
        }
    }

    public void createNewTab(Tab tab) {
        libraryTabs.getTabs().add(tab);
        libraryTabs.getSelectionModel().select(tab);
    }

    public void closeTab(Tab tab) {
        libraryTabs.getTabs().remove(tab);
    }

    public EventHandlerLibrary getEventHandler() {
        return eventHandlerLibrary;
    }

    public void update(LibraryWindowEntity argument) {
        Platform.runLater(() -> applicationListWidget.setItems(argument.getInstalledApplicationEntity()));
    }
}
