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

package org.phoenicis.javafx.views.mainwindow;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.phoenicis.javafx.JavaFXApplication;
import org.phoenicis.javafx.views.common.PhoenicisScene;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.apps.ViewApps;
import org.phoenicis.javafx.views.mainwindow.containers.ViewContainers;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import org.phoenicis.javafx.views.mainwindow.library.ViewLibrary;
import org.phoenicis.javafx.views.mainwindow.settings.ViewSettings;
import org.phoenicis.settings.SettingsManager;

public class MainWindow extends Stage {
    private final ViewLibrary library;
    private final ViewApps apps;
    private final ViewEngines engines;
    private final ViewContainers containers;
    private final ViewSettings settings;
    private final PhoenicisScene scene;

    private TabPane tabPane;

    public MainWindow(String applicationName, ViewLibrary library, ViewApps apps, ViewEngines engines,
            ViewContainers containers, ViewSettings settings, ThemeManager themeManager,
            SettingsManager settingsManager) {
        super();

        this.library = library;
        this.apps = apps;
        this.engines = engines;
        this.containers = containers;
        this.settings = settings;

        tabPane = new TabPane();
        tabPane.setId("menuPane");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab logoTab = new Tab();
        HBox logoLayout = new HBox();
        Region logo = new Region();
        logo.setId("logo");
        logo.setCursor(Cursor.DEFAULT);
        Label logoText = new Label(applicationName.toLowerCase());
        logoText.setId("logoText");
        logoText.setCursor(Cursor.DEFAULT);
        logo.prefWidthProperty().bind(logoText.heightProperty());
        logoLayout.getChildren().addAll(logo, logoText);
        logoTab.setGraphic(logoLayout);
        logoTab.setDisable(true);

        tabPane.getTabs().addAll(logoTab, library, apps, containers, engines, settings);

        this.scene = new PhoenicisScene(tabPane, themeManager, settingsManager);

        this.getIcons().add(new Image(
                JavaFXApplication.class.getResourceAsStream("/org/phoenicis/javafx/views/common/phoenicis.png")));

        this.setResizable(true);
        this.setScene(scene);
        this.setTitle(applicationName);
        this.show();
    }

    public void showLibrary() {
        tabPane.getSelectionModel().select(1);
    }
}
