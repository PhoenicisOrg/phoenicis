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

import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.playonlinux.common.api.services.RemoteAvailableInstallers;
import com.playonlinux.ui.impl.javafx.common.HtmlTemplate;
import com.playonlinux.ui.impl.javafx.common.PlayOnLinuxScene;
import com.playonlinux.ui.impl.javafx.common.ProgressButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
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
    private Scene failureScene;

    private RemoteAvailableInstallers availableInstallers;
    private final HeaderPane header;
    private AvailableInstallerListWidget availableInstallerListWidget;

    private TextField searchWidget;
    private CheckBox testingCheck;
    private CheckBox noCdNeededCheck;
    private CheckBox commercialCheck;

    private WebView descriptionWidget;
    private ImageView miniatureWidget;
    private ProgressButton installButton;
    private Button refreshButton;
    private Button retryButton;

    public AvailableInstallerListWidget getAvailableInstallerListWidget() {
        return availableInstallerListWidget;
    }
    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent The parent window
     * @return the install window instance
     */
    public static InstallWindow getInstance(PlayOnLinuxWindow parent) throws PlayOnLinuxException {
        if(instance == null) {
            instance = new InstallWindow(parent);
        } else {
            instance.toFront();
        }

        return instance;
    }

    private InstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxException {
        super();
        this.parent = parent;
        this.availableInstallers = this.eventHandler.getRemoteAvailableInstallers();
        header = new HeaderPane(this.eventHandler);

        this.setUpMainScene();
        this.setUpUpdateScene();
        this.setUpFailureScene();

        this.update(availableInstallers);
        this.setUpEvents();
        this.show();
    }

    private void setUpMainScene() {
        BorderPane mainPane = new BorderPane();
        mainScene = new PlayOnLinuxScene(mainPane, 800, 545);

        BorderPane centerPane = new BorderPane();
        centerPane.setPadding(new Insets(10, 10, 0, 10));

        HBox filterPane = new HBox();
        filterPane.setAlignment(Pos.CENTER_LEFT);
        filterPane.setSpacing(20);
        searchWidget = new TextField();
        searchWidget.setPrefWidth(250);
        searchWidget.setPromptText(translate("Search"));
        Label filterLbl = new Label(translate("Include") + ":");
        testingCheck = new CheckBox(translate("Testing"));
        noCdNeededCheck = new CheckBox(translate("No CD needed"));
        commercialCheck = new CheckBox(translate("Commercial"));
        commercialCheck.setSelected(true);
        filterPane.getChildren().addAll(searchWidget, filterLbl, testingCheck, noCdNeededCheck, commercialCheck);

        try {
            availableInstallerListWidget = new AvailableInstallerListWidget(eventHandler);
        } catch (PlayOnLinuxException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to initialize available installers."));
            alert.setContentText(String.format("The error was: %s", e.toString()));
            alert.show();
            e.printStackTrace();
        }

        VBox sidePane = new VBox();
        sidePane.setPadding(new Insets(0, 10, 0, 10));
        sidePane.setPrefWidth(230);
        sidePane.setSpacing(10);
        sidePane.setAlignment(Pos.CENTER);
        descriptionWidget = new WebView();
        miniatureWidget = new ImageView(new Image(getClass().getResourceAsStream("defaultMiniature.png")));
        miniatureWidget.prefHeight(150);
        sidePane.getChildren().addAll(descriptionWidget, miniatureWidget);

        centerPane.setTop(filterPane);
        centerPane.setMargin(filterPane, new Insets(0, 0, 10, 0));
        centerPane.setCenter(availableInstallerListWidget);
        centerPane.setRight(sidePane);

        HBox bottomPane = new HBox();
        bottomPane.setSpacing(10);
        bottomPane.setPadding(new Insets(5));
        bottomPane.setAlignment(Pos.CENTER_RIGHT);

        ImageView installImage = new ImageView(new Image(getClass().getResourceAsStream("install.png")));
        installImage.setFitWidth(16);
        installImage.setFitHeight(16);
        installButton = new ProgressButton(translate("Install"), installImage);

        installButton.setDisable(true);

        ImageView updateImage = new ImageView(new Image(getClass().getResourceAsStream("refresh.png")));
        updateImage.setFitWidth(16);
        updateImage.setFitHeight(16);
        refreshButton = new Button(translate("Refresh"), updateImage);
        bottomPane.getChildren().addAll(installButton, refreshButton);

        mainPane.setTop(header);
        mainPane.setCenter(centerPane);
        mainPane.setBottom(bottomPane);

    }

    private void setUpUpdateScene() {
        VBox updatePane = new VBox();
        updatePane.setAlignment(Pos.CENTER);
        updateScene = new PlayOnLinuxScene(updatePane, 800, 545);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefWidth(64);
        progressIndicator.setPrefHeight(64);

        updatePane.getChildren().add(progressIndicator);
    }

    private void setUpFailureScene() {
        BorderPane failurePane = new BorderPane();
        failureScene = new PlayOnLinuxScene(failurePane, 800, 545);
        VBox centerRegion = new VBox();
        centerRegion.setSpacing(10);
        centerRegion.setAlignment(Pos.CENTER);

        Label failureNotificationLbl = new Label();
        failureNotificationLbl.setText(translate("Connecting to ${application.name} failed.\n" +
                "Please check your connection and try again."));
        failureNotificationLbl.setTextAlignment(TextAlignment.CENTER);

        ImageView retryImage = new ImageView(new Image(getClass().getResourceAsStream("refresh.png")));
        retryImage.setFitWidth(16);
        retryImage.setFitHeight(16);
        retryButton = new Button(translate("Retry"), retryImage);

        centerRegion.getChildren().addAll(failureNotificationLbl, retryButton);
        failurePane.setCenter(centerRegion);
    }



    private void showMainScene() {
        this.setScene(mainScene);
    }

    private void showUpdateScene() {
        this.setScene(updateScene);
    }

    private void showFailureScene() {
        this.setScene(failureScene);
    }


    private void setUpEvents() throws PlayOnLinuxException {
        availableInstallers.addObserver(this);
        availableInstallers.addObserver(header);
        eventHandler.getRemoteInstallerObservable().addObserver(installButton);
        eventHandler.getRemoteAvailableInstallers().addObserver(availableInstallerListWidget);

        availableInstallerListWidget.addChangeListener(newValue -> {
            try {
                if (newValue == null || StringUtils.isBlank(newValue)) {
                    installButton.setEnableIfPosible(false);
                } else {
                    installButton.setEnableIfPosible(true);
                    try {
                        descriptionWidget.getEngine().loadContent(
                                new HtmlTemplate(this.getClass().getResource("descriptionTemplate.html"))
                                        .render(eventHandler.getInstallerDescription(newValue))
                        );
                    } catch (IOException e) {
                        throw new PlayOnLinuxException("Error while loading descriptionTemplate.html", e);
                    }
                }
            } catch (PlayOnLinuxException playOnLinuxException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to get installer information."));
                alert.setContentText(String.format("The error was: %s", playOnLinuxException.toString()));
                alert.show();
                playOnLinuxException.printStackTrace();
            }
        });
        searchWidget.setOnKeyPressed(event -> {
            availableInstallerListWidget.setSearchFilter(searchWidget.getText());
            eventHandler.refreshAvailableInstallerLisWidget();
        });
        testingCheck.setOnAction(event -> availableInstallerListWidget.setIncludeTesting(testingCheck.isSelected()));
        noCdNeededCheck.setOnAction(event -> availableInstallerListWidget.setIncludeNoCDNeeded(noCdNeededCheck.isSelected()));
        commercialCheck.setOnAction(event -> availableInstallerListWidget.setIncludeCommercial(commercialCheck.isSelected()));

        availableInstallerListWidget.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                eventHandler.installProgram(availableInstallerListWidget.getSelectedItemLabel());
            }
        });
        installButton.setOnMouseClicked(event -> eventHandler.installProgram(availableInstallerListWidget.getSelectedItemLabel()));

        refreshButton.setOnMouseClicked(event -> eventHandler.updateAvailableInstallers());
        retryButton.setOnMouseClicked(event -> eventHandler.updateAvailableInstallers());


        this.setOnCloseRequest(event -> {
            availableInstallers.deleteObserver(this);
            availableInstallers.deleteObserver(header);
            eventHandler.getRemoteAvailableInstallers().deleteObserver(availableInstallerListWidget);
            eventHandler.getRemoteInstallerObservable().deleteObserver(installButton);

            instance = null;
        });
    }



    public void update(RemoteAvailableInstallers remoteAvailableInstallers) {
        if(remoteAvailableInstallers.isUpdating()) {
            this.showUpdateScene();
        } else if(remoteAvailableInstallers.hasFailed()) {
            this.showFailureScene();
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

