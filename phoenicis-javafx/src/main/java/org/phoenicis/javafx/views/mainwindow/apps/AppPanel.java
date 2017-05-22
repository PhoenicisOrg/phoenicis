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

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.ScriptDTO;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.settings.SettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

final class AppPanel extends DetailsView {
    private final Logger LOGGER = LoggerFactory.getLogger(AppPanel.class);

    private final ApplicationDTO application;
    private final ThemeManager themeManager;
    private final SettingsManager settingsManager;

    private Consumer<ScriptDTO> onScriptInstall;

    public AppPanel(ApplicationDTO application, ThemeManager themeManager, SettingsManager settingsManager) {
        super();

        this.application = application;
        this.themeManager = themeManager;
        this.settingsManager = settingsManager;

        this.setTitle(application.getName());

        this.getStyleClass().addAll("appPresentation");

        this.populateCenter();
    }

    private void populateCenter() {
        final VBox descriptionWidget = new VBox();

        WebView appDescription = new WebView();
        VBox.setVgrow(appDescription, Priority.ALWAYS);
        appDescription.getEngine().loadContent("<body>" + application.getDescription() + "</body>");

        final URL style = getClass().getResource(String.format("/org/phoenicis/javafx/themes/%s/description.css",
                themeManager.getCurrentTheme().getShortName()));
        appDescription.getEngine().setUserStyleSheetLocation(style.toString());
        Label installers = new Label("Installers");
        installers.getStyleClass().add("descriptionTitle");

        GridPane grid = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(100);
        grid.getColumnConstraints().addAll(column1, column2);
        int row = 0;
        for (ScriptDTO script : application.getScripts()) {
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
            grid.addRow(row, scriptName, installButton);
            row++;
        }

        descriptionWidget.getChildren().addAll(appDescription, installers, grid);

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

        this.setCenter(new VBox(descriptionWidget, miniaturesPaneWrapper));
    }

    public void setOnScriptInstall(Consumer<ScriptDTO> onScriptInstall) {
        this.onScriptInstall = onScriptInstall;
    }

}
