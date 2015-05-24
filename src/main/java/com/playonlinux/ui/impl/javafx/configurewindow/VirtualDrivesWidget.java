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

import com.playonlinux.common.dto.VirtualDriveDTO;
import com.playonlinux.ui.impl.javafx.common.SimpleIconListWidget;
import javafx.application.Platform;

import java.util.Observable;
import java.util.Observer;


public class VirtualDrivesWidget extends SimpleIconListWidget implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        this.clear();
        Platform.runLater(() -> {
            Iterable<VirtualDriveDTO> virtualDrives = (Iterable<VirtualDriveDTO>) o;
            for (VirtualDriveDTO virtualDrive : virtualDrives) {
                addItem(virtualDrive.getName(), virtualDrive.getIcon());
            }
        });
    }


}
