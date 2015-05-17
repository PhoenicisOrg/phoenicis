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

import java.net.MalformedURLException;

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
        installWindow.getAvailableInstallerListWidget().setCategoryName(categoryName);
    }

    public String getInstallerDescription(String scriptName) throws PlayOnLinuxError {
        return getRemoteAvailableInstallers().getScriptByName(scriptName).getDescription();
    }

    public void installProgram(String selectedItemLabel) {
        System.out.println(selectedItemLabel);
    }
}
