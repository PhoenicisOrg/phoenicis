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

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
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

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The {@link DetailsView} for the "Apps" tab.
 * This view contains the information about a selected application and its scripts.
 *
 * @since 30.05.17
 */
final class AppPanel extends DetailsView {
    private final Logger LOGGER = LoggerFactory.getLogger(AppPanel.class);

    /**
     * The application to be shown inside this {@link AppPanel}
     */
    private final ApplicationDTO application;

    private final ThemeManager themeManager;
    private final SettingsManager settingsManager;

    /**
     * The list of shown scripts belonging to the <code>application</code> shown inside this {@link AppPanel}.
     * These scripts are filtered using the settings the user made inside {@link ApplicationSideBar}
     */
    private FilteredList<ScriptDTO> filteredScripts;

    /**
     * The container for the content inside this {@link AppPanel}
     */
    private VBox center;

    /**
     * A {@link WebView} containing the description for the <code>application</code>
     */
    private WebView appDescription;

    /**
     * A label with the "Installer" section header
     */
    private Label installers;

    /**
     * A grid containing all scripts and their corresponding install buttons
     */
    private GridPane scriptGrid;

    /**
     * A container for the miniatures for the <code>application</code>
     */
    private HBox miniaturesPane;

    /**
     * A scroll pane wrapping <code>miniaturesPane</code>
     */
    private ScrollPane miniaturesPaneWrapper;

    /**
     * An event consumer function, to called whenever an "install" button for a script has been clicked.
     */
    private Consumer<ScriptDTO> onScriptInstall;

    /**
     * Constructor
     *
     * @param application     The application to be shown inside this {@link AppPanel}
     * @param filter          The filter to be used for filtering the shown scripts for the <code>application</code>
     * @param themeManager    The theme manager
     * @param settingsManager The settings manager
     */
    public AppPanel(ApplicationDTO application, ApplicationFilter filter, ThemeManager themeManager,
            SettingsManager settingsManager) {
        super();

        this.application = application;
        this.themeManager = themeManager;
        this.settingsManager = settingsManager;

        this.filteredScripts = new FilteredList<>(FXCollections.observableArrayList(application.getScripts()));
        this.filteredScripts.predicateProperty().bind(filter.scriptFilterProperty());

        this.setTitle(application.getName());

        this.getStyleClass().addAll("appPresentation");

        this.populateCenter();
    }

    private void populateCenter() {
        this.appDescription = new WebView();
        this.appDescription.getEngine().loadContent("<body>" + application.getDescription() + "</body>");

        themeManager.bindWebEngineStylesheet(appDescription.getEngine().userStyleSheetLocationProperty());

        this.installers = new Label(tr("Installers"));
        this.installers.getStyleClass().add("descriptionTitle");

        this.scriptGrid = new GridPane();

        filteredScripts.addListener((InvalidationListener) change -> this.refreshScripts());
        this.refreshScripts();

        this.miniaturesPane = new HBox();
        this.miniaturesPane.getStyleClass().add("appPanelMiniaturesPane");

        this.miniaturesPaneWrapper = new ScrollPane(miniaturesPane);
        this.miniaturesPaneWrapper.getStyleClass().add("appPanelMiniaturesPaneWrapper");

        for (URI miniatureUri : application.getMiniatures()) {
            Region image = new Region();
            image.getStyleClass().add("appMiniature");
            image.setStyle(String.format("-fx-background-image: url(\"%s\");", miniatureUri.toString()));

            image.prefHeightProperty().bind(miniaturesPaneWrapper.heightProperty().multiply(0.8));
            image.prefWidthProperty().bind(image.prefHeightProperty().multiply(1.5));

            miniaturesPane.getChildren().add(image);
        }

        this.center = new VBox(appDescription, installers, scriptGrid, miniaturesPaneWrapper);

        VBox.setVgrow(appDescription, Priority.ALWAYS);

        this.setCenter(center);
    }

    /**
     * Refreshes the shown scripts.
     * When this method is called it begins by clearing the <code>scriptGrid</code>.
     * Afterwards this method refills it.
     */
    private void refreshScripts() {
        scriptGrid.getChildren().clear();

        for (int i = 0; i < filteredScripts.size(); i++) {
            ScriptDTO script = filteredScripts.get(i);

            Label scriptName = new Label(script.getScriptName());
            if (settingsManager.isViewScriptSource()) {
                final Tooltip tooltip = new Tooltip(tr("Source: {0}", script.getScriptSource()));
                Tooltip.install(scriptName, tooltip);
            }

            Button installButton = new Button(tr("Install"));
            installButton.setOnMouseClicked(evt -> {
                try {
                    onScriptInstall.accept(script);
                } catch (IllegalArgumentException e) {
                    LOGGER.error("Failed to get script", e);
                    new ErrorMessage(tr("Error while trying to download the installer"), e).show();
                }
            });

            scriptGrid.addRow(i, scriptName, installButton);

            GridPane.setHgrow(scriptName, Priority.ALWAYS);
        }
    }

    /**
     * Updates the event consumer, which is called when an install button for a script has been clicked
     *
     * @param onScriptInstall The new event consumer
     */
    public void setOnScriptInstall(Consumer<ScriptDTO> onScriptInstall) {
        this.onScriptInstall = onScriptInstall;
    }
}
