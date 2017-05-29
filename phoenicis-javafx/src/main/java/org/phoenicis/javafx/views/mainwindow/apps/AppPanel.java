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

package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.settings.SettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.function.Consumer;

final class AppPanel extends DetailsView {
    private final Logger LOGGER = LoggerFactory.getLogger(AppPanel.class);

    private final ApplicationDTO application;

    private final ThemeManager themeManager;
    private final SettingsManager settingsManager;

    private Consumer<ScriptDTO> onScriptInstall;

    private FilteredList<ScriptDTO> filteredScripts;

    public AppPanel(ApplicationDTO application, ApplicationFilter filter, ThemeManager themeManager, SettingsManager settingsManager) {
        super();

        this.application = application;
        this.themeManager = themeManager;
        this.settingsManager = settingsManager;

        this.filteredScripts = new FilteredList<ScriptDTO>(FXCollections.observableArrayList(application.getScripts()));
        this.filteredScripts.predicateProperty().bind(filter.scriptFilterProperty());

        this.setTitle(application.getName());

        this.getStyleClass().addAll("appPresentation");

        this.populateCenter();
    }

    private void populateCenter() {
        WebView appDescription = new WebView();
        appDescription.getEngine().loadContent("<body>" + application.getDescription() + "</body>");

        themeManager.bindWebEngineStylesheet(appDescription.getEngine().userStyleSheetLocationProperty());

        Label installers = new Label("Installers");
        installers.getStyleClass().add("descriptionTitle");

        ListView<ScriptDTO> scriptList = new ListView<>(filteredScripts);
        scriptList.setPrefHeight(0);
        scriptList.setMinHeight(100);
        scriptList.setCellFactory(value -> new ListCell<ScriptDTO>() {
            @Override
            public void updateItem(ScriptDTO script, boolean empty) {
                super.updateItem(script, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Label scriptName = new Label(script.getScriptName());
                    if (settingsManager.isViewScriptSource()) {
                        final Tooltip tooltip = new Tooltip(String.format("Source: %s", script.getScriptSource()));
                        Tooltip.install(scriptName, tooltip);
                    }

                    Button installButton = new Button("Install");
                    installButton.setOnMouseClicked(evt -> {
                        try {
                            onScriptInstall.accept(script);
                        } catch (IllegalArgumentException e) {
                            LOGGER.error("Failed to get script", e);
                            new ErrorMessage("Error while trying to download the installer", e).show();
                        }
                    });

                    GridPane container = new GridPane();

                    container.add(scriptName, 0, 0);
                    container.add(installButton, 1, 0);

                    GridPane.setHgrow(scriptName, Priority.ALWAYS);

                    setGraphic(container);
                }
            }
        });

        final HBox miniaturesPane = new HBox();
        miniaturesPane.getStyleClass().add("appPanelMiniaturesPane");

        final ScrollPane miniaturesPaneWrapper = new ScrollPane(miniaturesPane);
        miniaturesPaneWrapper.getStyleClass().add("appPanelMiniaturesPaneWrapper");

        for (URI miniatureUri : application.getMiniatures()) {
            Region image = new Region();
            image.getStyleClass().add("appMiniature");
            image.setStyle(String.format("-fx-background-image: url(\"%s\");", miniatureUri.toString()));

            image.prefHeightProperty().bind(miniaturesPaneWrapper.heightProperty().multiply(0.8));
            image.prefWidthProperty().bind(image.prefHeightProperty().multiply(1.5));

            miniaturesPane.getChildren().add(image);
        }

        VBox center = new VBox(appDescription, installers, scriptList, miniaturesPaneWrapper);

        VBox.setVgrow(appDescription, Priority.ALWAYS);

        this.setCenter(center);
    }

    public void setOnScriptInstall(Consumer<ScriptDTO> onScriptInstall) {
        this.onScriptInstall = onScriptInstall;
    }

}
