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

package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.phoenicis.javafx.JavaFXApplication;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.PhoenicisScene;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsView;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersView;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesView;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsView;
import org.phoenicis.javafx.views.mainwindow.library.LibraryView;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsView;

public class MainWindow extends Stage {
    private final LibraryView library;
    private final ApplicationsView apps;
    private final EnginesView engines;
    private final ContainersView containers;
    private final SettingsView settings;
    private final PhoenicisScene scene;

    private TabPane tabPane;

    public MainWindow(String applicationName,
            LibraryView library,
            ApplicationsView apps,
            EnginesView engines,
            ContainersView containers,
            InstallationsView installations,
            SettingsView settings,
            ThemeManager themeManager,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.library = library;
        this.apps = apps;
        this.engines = engines;
        this.containers = containers;
        this.settings = settings;

        tabPane = new TabPane();
        tabPane.setId("menuPane");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(library, apps, containers, engines, installations, settings);

        this.scene = new PhoenicisScene(tabPane, themeManager, javaFxSettingsManager);

        this.getIcons().add(new Image(
                JavaFXApplication.class.getResourceAsStream("/org/phoenicis/javafx/views/common/phoenicis.png")));

        // avoid 1x1 pixel window
        this.setMinHeight(200);
        this.setMinWidth(200);
        this.setResizable(true);
        this.setHeight(javaFxSettingsManager.getWindowHeight());
        this.setWidth(javaFxSettingsManager.getWindowWidth());
        this.setMaximized(javaFxSettingsManager.isWindowMaximized());
        this.setScene(scene);
        this.setTitle(applicationName);
        this.show();
    }

    public void showInstallations() {
        tabPane.getSelectionModel().select(4);
    }
}
