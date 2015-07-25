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

package com.playonlinux.ui.impl.javafx.mainwindow.library;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.python.CommandInterpreterException;
import com.playonlinux.library.dto.InstalledApplicationDTO;
import com.playonlinux.library.dto.LibraryWindowDTO;
import com.playonlinux.library.LibraryFilter;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.ui.impl.javafx.mainwindow.console.ConsoleTab;
import com.playonlinux.ui.impl.javafx.mainwindow.*;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.File;

import static com.playonlinux.core.lang.Localisation.translate;

public class ViewLibrary extends MainWindowView implements Observer<Observable, LibraryWindowDTO> {

    private LeftButton runScript;
    private LeftButton runConsole;
    private static final Logger LOGGER = Logger.getLogger(ViewLibrary.class);
    private ApplicationListWidget applicationListWidget;
    private final EventHandlerLibrary eventHandlerLibrary;
    private TextField searchBar;

    private final  EntitiesProvider<InstalledApplicationDTO, LibraryWindowDTO> libraryItems;
    private TabPane libraryTabs;


    public ViewLibrary(MainWindow parent) {
        super(parent);
        this.getStyleClass().add("mainWindowScene");

        eventHandlerLibrary = new EventHandlerLibrary();
        libraryItems = eventHandlerLibrary.getInstalledApplications();

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

    protected void drawSideBar() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> applyFilter(searchBar.getText()));

        this.runScript = new LeftButton("/com/playonlinux/ui/impl/javafx/mainwindow/library/script.png", translate("Run a script"));
        this.runConsole = new LeftButton("/com/playonlinux/ui/impl/javafx/mainwindow/library/console.png", translate("${application.name} console"));

        this.runScript.getStyleClass().add("leftPaneButtons");
        this.runConsole.getStyleClass().add("leftPaneButtons");

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Advanced tools"), runScript, runConsole);

        super.drawSideBar();
    }

    private void applyFilter(String searchText) {
        libraryItems.applyFilter(new LibraryFilter(searchText));
    }

    public void setUpEvents() {
        libraryItems.addObserver(this);

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
            final Tab console = new ConsoleTab();
            libraryTabs.getTabs().add(console);
            libraryTabs.getSelectionModel().select(console);
        } catch (CommandInterpreterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to run the console."));
            alert.setContentText("The error was: " + ExceptionUtils.getFullStackTrace(e));
            LOGGER.warn("Error while trying to run the console", e);
        }


    }

    public EventHandlerLibrary getEventHandler() {
        return eventHandlerLibrary;
    }

    @Override
    public void update(Observable observable, LibraryWindowDTO argument) {
        Platform.runLater(() -> applicationListWidget.setItems(argument.getInstalledApplicationDTO()));
    }
}
