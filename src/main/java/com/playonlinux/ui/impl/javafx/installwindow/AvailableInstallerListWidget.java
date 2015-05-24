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
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.ui.impl.javafx.common.SimpleIconListWidget;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.commons.lang.StringUtils;

import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;

public class AvailableInstallerListWidget extends SimpleIconListWidget implements Observer {
    private final InstallWindowEventHandler installWindowEventHandler;
    private RemoteAvailableInstallers remoteAvailableInstallers;

    public RemoteAvailableInstallers.InstallerFilter getFilter(){
        return remoteAvailableInstallers.getFilter();
    }

//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//        this.update();
//    }
//
//    public void setIncludeCommercial(boolean includeCommercial) {
//        this.includeCommercial = includeCommercial;
//        this.update();
//    }
//
//    public void setIncludeNoCDNeeded(boolean includeNoCDNeeded) {
//        this.includeNoCDNeeded = includeNoCDNeeded;
//        this.update();
//    }
//
//    public void setIncludeTesting(boolean includeTesting) {
//        this.includeTesting = includeTesting;
//        this.update();
//    }
//
//    public void setSearchFilter(String searchFilter) {
//        this.searchFilter = searchFilter;
//        this.update();
//    }
//
//    private String categoryName;
//    private boolean includeTesting = false;
//    private boolean includeNoCDNeeded = false;
//    private boolean includeCommercial = true;
//    private String searchFilter = "";

    AvailableInstallerListWidget(InstallWindowEventHandler installWindowEventHandler) throws PlayOnLinuxError {
        super();
        this.installWindowEventHandler = installWindowEventHandler;
        this.installWindowEventHandler.getRemoteAvailableInstallers().addObserver(this);
        remoteAvailableInstallers = this.installWindowEventHandler.getRemoteAvailableInstallers();

        remoteAvailableInstallers.getFilter().addObserver((observable, o) -> update());
    }

    public void update() {
        if(remoteAvailableInstallers != null) {
            this.clear();
            try {
                for (ScriptDTO script : remoteAvailableInstallers.getFilteredList()) {
                    this.addItem(script.getName());
                }
            } catch (PlayOnLinuxError playOnLinuxError) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to show installer list."));
                alert.setContentText(String.format("The error was: %s", playOnLinuxError));
                playOnLinuxError.printStackTrace();
                alert.show();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        Platform.runLater(() -> update());
    }
}