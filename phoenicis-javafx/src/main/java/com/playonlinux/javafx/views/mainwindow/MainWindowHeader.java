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

package com.playonlinux.javafx.views.mainwindow;

import com.playonlinux.javafx.views.common.widget.PlayOnLinuxLogo;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MainWindowHeader extends GridPane {
    private final Tab myApps;
    private final Tab appCenter;
    private final Tab engines;
    private final Tab settings;
    private final Tab containers;

    public MainWindowHeader(PlayOnLinuxLogo playOnLinuxLogo) {
        super();

        ColumnConstraints logoConstraint = new ColumnConstraints();
        logoConstraint.setPercentWidth(30);

        ColumnConstraints menuConstraint = new ColumnConstraints();
        menuConstraint.setPercentWidth(70);

        this.getColumnConstraints().addAll(logoConstraint, menuConstraint);


        this.setId("headerPane");


        this.add(playOnLinuxLogo, 0, 0);

        TabPane menuPane = new TabPane();
        menuPane.setId("menuPane");
        menuPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        this.add(menuPane, 1, 0);

        myApps = new Tab("Library");

        appCenter = new Tab("Apps");

        containers = new Tab("Containers");

        engines = new Tab("Engines");

        settings = new Tab("Settings");

        menuPane.getTabs().addAll(myApps, appCenter, containers, engines, settings);
    }

    private void setLinkEvents(Tab tab, EventHandler<Event> eventHandler) {
        tab.setOnSelectionChanged(eventHandler);
    }

    public void setLibraryEvent(EventHandler<Event> eventHandler) {
        setLinkEvents(myApps, eventHandler);
    }

    public void setAppsEvent(EventHandler<Event> eventHandler) {
        setLinkEvents(appCenter, eventHandler);
    }
    public void setEnginesEvent(EventHandler<Event> eventHandler) {
        setLinkEvents(engines, eventHandler);
    }
    public void setContainersEvent(EventHandler<Event> eventHandler) {
        setLinkEvents(containers, eventHandler);
    }
    public void setSettingsEvent(EventHandler<Event> eventHandler) {
        setLinkEvents(settings, eventHandler);
    }

}
