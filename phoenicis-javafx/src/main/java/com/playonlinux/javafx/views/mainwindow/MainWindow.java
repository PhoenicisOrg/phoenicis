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

package com.playonlinux.javafx.views.mainwindow;

import com.playonlinux.javafx.JavaFXApplication;
import com.playonlinux.javafx.views.common.PlayOnLinuxScene;
import com.playonlinux.javafx.views.common.widget.PlayOnLinuxLogo;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindow extends Stage {
    private final ViewLibrary library;
    private final ViewApps apps;
    private final ViewEngines engines;
    private final ViewContainers containers;
    private final ViewSettings settings;
    private final PlayOnLinuxScene scene;

    private TabPane tabPane;


    public MainWindow(String applicationName,
                      String theme,
                      ViewLibrary library,
                      ViewApps apps,
                      ViewEngines engines,
                      ViewContainers containers,
                      ViewSettings settings,
                      PlayOnLinuxLogo playOnLinuxLogo) {
        super();

        this.library = library;
        this.apps = apps;
        this.engines = engines;
        this.containers = containers;
        this.settings = settings;

        tabPane = new TabPane();
        tabPane.setTabMinHeight(50);
        tabPane.setId("menuPane");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab logoTab = new Tab();
        playOnLinuxLogo.setMinWidth(200);
        logoTab.setGraphic(playOnLinuxLogo);
        logoTab.setDisable(true);

        tabPane.getTabs().addAll(logoTab, library, apps, containers, engines, settings);

        scene = new PlayOnLinuxScene(tabPane, theme);

        this.getIcons().add(new Image(JavaFXApplication.class.getResourceAsStream("/com/playonlinux/javafx/views/common/playonlinux.png")));

        this.setResizable(true);
        this.setScene(scene);
        this.setTitle(applicationName);
        this.show();

        this.setUpEvents();
    }

    private void setUpEvents() {
        library.setUpEvents();
        engines.setUpEvents();
    }

    public void showLibrary() {
        tabPane.getSelectionModel().select(1);
    }
}
