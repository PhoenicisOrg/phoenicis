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

import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.ui.impl.javafx.mainwindow.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.playonlinux.domain.Localisation.translate;

public class ViewLibrary extends HBox {

    private LeftButton runScript;
    private final MainWindow parent;
    private Logger logger = Logger.getLogger(this.getClass());
    private ApplicationListWidget applicationListWidget;
    private final EventHandlerMyApps eventHandlerMyApps;


    public ViewLibrary(MainWindow parent) {
        this.parent = parent;
        this.getStyleClass().add("mainWindowScene");

        this.drawSideBar();
        this.drawContent();
        eventHandlerMyApps = new EventHandlerMyApps();
    }

    private void drawContent() {
        applicationListWidget = new ApplicationListWidget(this);
        applicationListWidget.getStyleClass().add("rightPane");


        this.getChildren().add(applicationListWidget);
    }

    private void drawSideBar() {
        LeftSideBar leftContent = new LeftSideBar();

        this.getChildren().add(leftContent);

        TextField searchBar = new TextField();

        this.runScript = new LeftButton("/com/playonlinux/ui/impl/javafx/mainwindow/library/script.png", "Run a script");

        LeftSpacer spacer = new LeftSpacer();
        leftContent.getChildren().addAll(searchBar, spacer, new LeftBarTitle("Advanced tools"), runScript);
    }

    public void setUpEvents() {
        try {
            eventHandlerMyApps.getInstalledApplications().addObserver(applicationListWidget);
        } catch (PlayOnLinuxException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to update installer list."));
            alert.setContentText(String.format("The error was: %s", e));
            alert.show();
            logger.error(e);
        }

        runScript.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");
            File scriptToRun = fileChooser.showOpenDialog(parent);

            try {
                if (scriptToRun != null) {
                    eventHandlerMyApps.runLocalScript(scriptToRun);
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to run the script."));
                alert.setContentText("The file was not found");
                logger.warn("Error while trying to run the script", e);
            }
        });
    }

    public EventHandlerMyApps getEventHandler() {
        return eventHandlerMyApps;
    }
}
