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

import com.playonlinux.common.dtos.ScriptDTO;
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.InstallerFilter;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.ui.impl.javafx.common.SimpleIconListWidget;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;

public class AvailableInstallerListWidget extends SimpleIconListWidget implements Observer {
    private final InstallWindowEventHandler installWindowEventHandler;
    private RemoteAvailableInstallers remoteAvailableInstallers;

    private final InstallerFilter filter = new InstallerFilter();

    public InstallerFilter getFilter() {
        return this.filter;
    }


    AvailableInstallerListWidget(InstallWindowEventHandler installWindowEventHandler) throws PlayOnLinuxError {
        super();
        this.installWindowEventHandler = installWindowEventHandler;
        this.installWindowEventHandler.getRemoteAvailableInstallers().addObserver(this);
        remoteAvailableInstallers = this.installWindowEventHandler.getRemoteAvailableInstallers();

        AvailableInstallerListWidget me = this;
        this.filter.addObserver((observable, o) -> me.update());
    }

    public void update() {
        if (remoteAvailableInstallers != null) {
            this.clear();
            try {
                for (ScriptDTO script : remoteAvailableInstallers.getAllScripts(filter)) {
                    this.addItem(script.getName());
                }
            } catch (PlayOnLinuxError playOnLinuxError) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to show installer list."));
                alert.setContentText(String.format("The error was: %s", playOnLinuxError));
                playOnLinuxError.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        Platform.runLater(() -> update());
    }
}
