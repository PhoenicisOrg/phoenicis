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
import com.playonlinux.ui.impl.javafx.common.HtmlTemplate;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;

public class InstallWindow extends Stage implements PlayOnLinuxWindow, Observer {
    private final PlayOnLinuxWindow parent;
    private static InstallWindow instance;
    private final InstallWindowEventHandler eventHandler = new InstallWindowEventHandler(this);
    private Scene mainScene;
    private Scene updateScene;

    private RemoteAvailableInstallers availableInstallers;
    private final HeaderPane header;
    private AvailableInstallerListWidget availableInstallerListWidget;
    private TextField searchWidget;
    private WebView descriptionWidget;
    private Button installButton;
    private Button refreshButton;

    public AvailableInstallerListWidget getAvailableInstallerListWidget() {
        return availableInstallerListWidget;
    }
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
        this.availableInstallers = this.eventHandler.getRemoteAvailableInstallers();
        header = new HeaderPane(this.eventHandler);


        this.setUpMainScene();
        this.setUpUpdateScene();

        this.update(availableInstallers);
        this.setUpEvents();
        this.show();
    }

    private void setUpMainScene() {
        // TODO: Improve this scene (get rid of absolute positioning, ...)

        Pane mainPane = new Pane();
        mainScene = new Scene(mainPane, 800, 545);
        mainScene.getStylesheets().add(this.getClass().getResource("installWindow.css").toExternalForm());

        searchWidget = new TextField();
        searchWidget.setLayoutY(77);
        searchWidget.setLayoutX(10);
        searchWidget.setPrefWidth(250);
        searchWidget.setPromptText(translate("Search"));

        try {
            availableInstallerListWidget = new AvailableInstallerListWidget(eventHandler);
        } catch (PlayOnLinuxError e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to initialize available installers."));
            alert.setContentText(String.format("The error was: %s", e.toString()));
            alert.show();
            e.printStackTrace();
        }

        availableInstallerListWidget.setLayoutY(112);
        availableInstallerListWidget.setLayoutX(10);
        availableInstallerListWidget.setPrefWidth(550);
        availableInstallerListWidget.setPrefHeight(385);

        descriptionWidget = new WebView();
        descriptionWidget.setLayoutX(570);
        descriptionWidget.setLayoutY(112);
        descriptionWidget.setPrefWidth(218);
        descriptionWidget.setPrefHeight(200);

        ImageView installImage = new ImageView(new Image(getClass().getResourceAsStream("install.png")));
        installImage.setFitWidth(16);
        installImage.setFitHeight(16);
        installButton = new Button(translate("Install"), installImage);
        installButton.setLayoutY(510);
        installButton.setDisable(true);

        ImageView updateImage = new ImageView(new Image(getClass().getResourceAsStream("refresh.png")));
        updateImage.setFitWidth(16);
        updateImage.setFitHeight(16);
        refreshButton = new Button(translate("Refresh"), updateImage);
        refreshButton.setLayoutY(510);

        mainPane.getChildren().addAll(header, availableInstallerListWidget, searchWidget,
                descriptionWidget, installButton, refreshButton);

    }


    private void setUpUpdateScene() {
        Pane updatePane = new Pane();
        updateScene = new Scene(updatePane, 800, 545);
        updateScene.getStylesheets().add(this.getClass().getResource("installWindow.css").toExternalForm());

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefWidth(64);
        progressIndicator.setPrefHeight(64);
        progressIndicator.setLayoutX((this.updateScene.getWidth() - 64) / 2);
        progressIndicator.setLayoutY((this.updateScene.getHeight() - 64) / 2);

        updatePane.getChildren().add(progressIndicator);
    }


    private void showMainScene() {
        this.setScene(mainScene);
        /* JavaFX only waits for a node to be displayed before calculating width */
        Platform.runLater(() -> installButton.setLayoutX(790 - installButton.getWidth()));
        Platform.runLater(() -> refreshButton.setLayoutX(780 - refreshButton.getWidth() - installButton.getWidth()));
    }

    private void showUpdateScene() {
        this.setScene(updateScene);
    }



    private void setUpEvents() throws PlayOnLinuxError {
        availableInstallers.addObserver(this);
        availableInstallerListWidget.addChangeListener(newValue -> {
            try {
                if (newValue == null || StringUtils.isBlank(newValue)) {
                    installButton.setDisable(true);
                } else {
                    installButton.setDisable(false);
                    try {
                        descriptionWidget.getEngine().loadContent(
                                new HtmlTemplate(this.getClass().getResource("descriptionTemplate.html"))
                                        .render(eventHandler.getInstallerDescription(newValue))
                        );
                    } catch (IOException e) {
                        throw new PlayOnLinuxError("Error while loading descriptionTemplate.html", e);
                    }
                }
            } catch (PlayOnLinuxError playOnLinuxError) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to get installer information."));
                alert.setContentText(String.format("The error was: %s", playOnLinuxError.toString()));
                alert.show();
                playOnLinuxError.printStackTrace();
            }
        });
        searchWidget.setOnKeyPressed(event -> availableInstallerListWidget.setSearchFilter(searchWidget.getText()));

        availableInstallerListWidget.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                eventHandler.installProgram(availableInstallerListWidget.getSelectedItemLabel());
            }
        });
        installButton.setOnMouseClicked(event -> eventHandler.installProgram(availableInstallerListWidget.getSelectedItemLabel()));

        refreshButton.setOnMouseClicked(event -> eventHandler.updateAvailableInstallers());
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

    public void clearSearch() {
        searchWidget.clear();
        availableInstallerListWidget.setSearchFilter("");
    }
}

