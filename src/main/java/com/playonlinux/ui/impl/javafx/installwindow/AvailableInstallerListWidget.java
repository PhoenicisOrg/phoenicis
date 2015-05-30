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

import com.playonlinux.common.api.services.RemoteAvailableInstallers;
import com.playonlinux.common.dto.ApplicationDTO;
import com.playonlinux.domain.PlayOnLinuxException;
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setIncludeCommercial(boolean includeCommercial) {
        this.includeCommercial = includeCommercial;
    }

    public void setIncludeNoCDNeeded(boolean includeNoCDNeeded) {
        this.includeNoCDNeeded = includeNoCDNeeded;
    }

    public void setIncludeTesting(boolean includeTesting) {
        this.includeTesting = includeTesting;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    private String categoryName;
    private boolean includeTesting = false;
    private boolean includeNoCDNeeded = false;
    private boolean includeCommercial = true;
    private String searchFilter = "";

    AvailableInstallerListWidget(InstallWindowEventHandler installWindowEventHandler) throws PlayOnLinuxException {
        super();
        this.installWindowEventHandler = installWindowEventHandler;
        remoteAvailableInstallers = this.installWindowEventHandler.getRemoteAvailableInstallers();
    }

    public void update() {
        if(remoteAvailableInstallers != null) {
            this.clear();
            if(!StringUtils.isBlank(searchFilter)) {
                for(ApplicationDTO applicationDTO : remoteAvailableInstallers.getAllScripts(searchFilter)) {
                    this.addItem(applicationDTO.getName());
                }
            } else if (categoryName != null) {
                try {
                    for(ApplicationDTO applicationDTO: remoteAvailableInstallers.getAllApplicationsInCategory(categoryName)) {
                        this.addItem(applicationDTO.getName());
                    }
                } catch (PlayOnLinuxException playOnLinuxException) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(translate("Error while trying to show installer list."));
                    alert.setContentText(String.format("The error was: %s", playOnLinuxException));
                    playOnLinuxException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        Platform.runLater(this::update);
    }
}
