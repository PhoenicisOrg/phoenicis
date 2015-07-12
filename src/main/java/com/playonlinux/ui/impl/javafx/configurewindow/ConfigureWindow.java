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

package com.playonlinux.ui.impl.javafx.configurewindow;

import com.playonlinux.services.virtualdrives.InstalledVirtualDrives;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigureWindow extends Stage implements PlayOnLinuxWindow {
    private final PlayOnLinuxWindow parent;
    private static ConfigureWindow instance;
    private final VirtualDrivesWidget installedVirtualDrivesWidget;
    private final ConfigureWindowEventHandler eventHandler = new ConfigureWindowEventHandler();
    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent: The parent window
     * @return the configureWindow instance
     */
    public static ConfigureWindow getInstance(PlayOnLinuxWindow parent) throws PlayOnLinuxException {
        if(instance == null) {
            instance = new ConfigureWindow(parent);
        } else {
            instance.toFront();
        }

        return instance;
    }

    private ConfigureWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxException {
        super();
        this.parent = parent;

        this.installedVirtualDrivesWidget = new VirtualDrivesWidget();

        GridPane gridPane = new GridPane();
        gridPane.add(installedVirtualDrivesWidget, 0, 0);

        Scene scene = new Scene(gridPane, 620, 400);

        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setPercentWidth(25);

        gridPane.getColumnConstraints().add(columnConstraint);

        this.setScene(scene);
        this.setUpEvents();
        this.show();
    }

    public void setUpEvents() throws PlayOnLinuxException {
        /*
        InstalledVirtualDrives installedVirtualDrives = getEventHandler().getInstalledVirtualDrives();
        installedVirtualDrives.addObserver(this.installedVirtualDrivesWidget);

        this.setOnCloseRequest(event -> {
            installedVirtualDrives.deleteObserver(this.installedVirtualDrivesWidget);
            instance = null;
        });
        */
    }

    public ConfigureWindowEventHandler getEventHandler() {
        return eventHandler;
    }
}

