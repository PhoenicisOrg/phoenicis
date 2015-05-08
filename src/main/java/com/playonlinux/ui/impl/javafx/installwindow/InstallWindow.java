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

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.ui.impl.configurewindow.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.JavaFXEventHandler;
import com.playonlinux.ui.impl.javafx.configurewindow.VirtualDrivesWidget;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InstallWindow extends Stage implements PlayOnLinuxWindow {
    private final PlayOnLinuxWindow parent;
    private static InstallWindow instance;

    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent
     * @return the install window instance
     */
    public static InstallWindow getInstance(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        if(instance == null) {
            instance = new InstallWindow(parent);
        } else {
            instance.toFront();
        }
        return instance;
    }

    private InstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        super();
        this.parent = parent;

        Scene scene = new Scene(new Pane(), 620, 400);

        this.setScene(scene);
        this.show();
    }


    @Override
    public JavaFXEventHandler getEventHandler() {
        return parent.getEventHandler();
    }
}

