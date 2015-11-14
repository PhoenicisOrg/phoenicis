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

package com.playonlinux.ui.impl.javafx.mainwindow;

import com.playonlinux.ui.impl.javafx.common.widget.PlayOnLinuxLogo;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MainWindowHeader extends GridPane {
    private static final String MENU_ENTRY_CSS_CLASS = "menuEntry";
    private final Text myApps;
    private final Text appCenter;
    private final Text engines;
    private final Text settings;
    private final Text containers;

    public MainWindowHeader() {
        super();

        ColumnConstraints logoConstraint = new ColumnConstraints();
        logoConstraint.setPercentWidth(30);

        ColumnConstraints menuConstraint = new ColumnConstraints();
        menuConstraint.setPercentWidth(70);

        this.getColumnConstraints().addAll(logoConstraint, menuConstraint);

        PlayOnLinuxLogo playOnLinuxLogo = new PlayOnLinuxLogo();

        this.setId("headerPane");

        this.add(playOnLinuxLogo, 0, 0);

        HBox menuPane = new HBox();
        menuPane.setId("menuPane");
        this.add(menuPane, 1, 0);

        myApps = new Text("Library");
        myApps.getStyleClass().add(MENU_ENTRY_CSS_CLASS);

        appCenter = new Text("Apps");
        appCenter.getStyleClass().add(MENU_ENTRY_CSS_CLASS);

        containers = new Text("Containers");
        containers.getStyleClass().add(MENU_ENTRY_CSS_CLASS);

        engines = new Text("Engines");
        engines.getStyleClass().add(MENU_ENTRY_CSS_CLASS);

        settings = new Text("Settings");
        settings.getStyleClass().add(MENU_ENTRY_CSS_CLASS);

        menuPane.getChildren().addAll(myApps, appCenter, containers, engines, settings);
    }

    private void setLinkEvents(Node node, EventHandler<MouseEvent> eventHandler) {
        node.setOnMouseClicked(eventHandler);
    }

    public void setLibraryEvent(EventHandler<MouseEvent> eventHandler) {
        setLinkEvents(myApps, eventHandler);
    }

    public void setAppsEvent(EventHandler<MouseEvent> eventHandler) {
        setLinkEvents(appCenter, eventHandler);
    }

    public void setEnginesEvent(EventHandler<MouseEvent> eventHandler) {
        setLinkEvents(engines, eventHandler);
    }

    public void setContainersEvent(EventHandler<MouseEvent> eventHandler) {
        setLinkEvents(containers, eventHandler);
    }

    public void setSettingsEvent(EventHandler<MouseEvent> eventHandler) {
        setLinkEvents(settings, eventHandler);
    }

}
