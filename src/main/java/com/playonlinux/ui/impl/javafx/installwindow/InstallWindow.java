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

package com.playonlinux.ui.impl.javafx.installwindow;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;

public class InstallWindow extends Stage implements PlayOnLinuxWindow, Observer {
    private final PlayOnLinuxWindow parent;
    private static InstallWindow instance;
    private final InstallWindowEventHandler eventHandler = new InstallWindowEventHandler();
    private Scene mainScene;
    private Scene updateScene;
    private RemoteAvailableInstallers availableInstallers;
    private final HeaderPane header;

    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent
     * @return the install window instance
     */
    public static InstallWindow getInstance(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        if(instance == null) {
            instance = new InstallWindow(parent);
        } else {
            instance.toFront();
        }
        instance.setOnCloseRequest(event -> {
            instance = null;
        });

        return instance;
    }

    private InstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        super();
        this.parent = parent;
        try {
            this.availableInstallers = this.eventHandler.getRemoteAvailableInstallers();
            header = new HeaderPane(this.eventHandler);
        } catch (MalformedURLException e) {
            throw new PlayOnLinuxError("URL seems to be malformed", e);
        }



        this.update(availableInstallers);
        this.setUpEvents();
        this.show();
    }

    private void showMainScene() {
        if(mainScene == null) {
            Pane mainPane = new Pane();
            mainScene = new Scene(mainPane, 800, 600);
            mainScene.getStylesheets().add(this.getClass().getResource("installWindow.css").toExternalForm());

            mainPane.getChildren().add(header);
        }

        this.setScene(mainScene);
    }

    private void showUpdateScene() {
        if(updateScene == null) {
            Pane updatePane = new Pane();
            updateScene = new Scene(updatePane, 800, 600);
            updateScene.getStylesheets().add(this.getClass().getResource("installWindow.css").toExternalForm());

            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setPrefWidth(64);
            progressIndicator.setPrefHeight(64);
            progressIndicator.setLayoutX((this.updateScene.getWidth() - 64) / 2);
            progressIndicator.setLayoutY((this.updateScene.getHeight() - 64) / 2);

            updatePane.getChildren().add(progressIndicator);
        }

        this.setScene(updateScene);
    }

    private void setUpEvents() throws PlayOnLinuxError {
        availableInstallers.addObserver(this);
    }

    public InstallWindowEventHandler getEventHandler() {
        return eventHandler;
    }

    public void update(RemoteAvailableInstallers remoteAvailableInstallers) {
        if(remoteAvailableInstallers.isUpdating()) {
            this.showUpdateScene();
        } else if(remoteAvailableInstallers.hasFailed()) {
            // TODO
        } else {
            this.showMainScene();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        RemoteAvailableInstallers remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        Platform.runLater(() -> update(remoteAvailableInstallers));
    }
}

