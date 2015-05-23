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
import com.playonlinux.injection.Scan;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.ui.api.UIEventHandler;
import javafx.scene.control.Alert;


import static com.playonlinux.domain.Localisation.translate;

@Scan
public class InstallWindowEventHandler implements UIEventHandler {
    private final InstallWindow installWindow;

    InstallWindowEventHandler(InstallWindow installWindow) {
        this.installWindow = installWindow;
    }

    @Inject
    static EventHandler mainEventHandler;

    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }

    public RemoteAvailableInstallers getRemoteAvailableInstallers() throws PlayOnLinuxError {
        return mainEventHandler.getRemoteAvailableInstallers();
    }

    public void selectCategory(String categoryName) {
        installWindow.getAvailableInstallerListWidget().getFilter().setCategory(categoryName);
    }

    public String getInstallerDescription(String scriptName) throws PlayOnLinuxError {
        return getRemoteAvailableInstallers().getScriptByName(scriptName).getDescription();
    }

    public void installProgram(String selectedItemLabel) {
        System.out.println(selectedItemLabel);
    }

    public void updateAvailableInstallers() {
        try {
            getRemoteAvailableInstallers().refresh();
        } catch (PlayOnLinuxError playOnLinuxError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to update installers."));
            alert.setContentText(String.format("The error was: %s", playOnLinuxError.toString()));
            alert.show();
            playOnLinuxError.printStackTrace();
        }
    }

    public void clearSearch() {
        installWindow.clearSearch();
    }
}
