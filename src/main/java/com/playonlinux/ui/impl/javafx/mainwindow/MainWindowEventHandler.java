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

import com.playonlinux.common.api.services.EventHandler;
import com.playonlinux.common.api.services.InstalledApplications;
import com.playonlinux.common.api.services.InstalledVirtualDrives;
import com.playonlinux.common.api.services.RemoteAvailableInstallers;
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.injection.Scan;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.*;
import com.playonlinux.ui.impl.javafx.configurewindow.ConfigureWindow;
import com.playonlinux.ui.impl.javafx.installwindow.InstallWindow;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Scan
class MainWindowEventHandler implements UIEventHandler {
    @Inject
    static EventHandler mainEventHandler;


    public InstalledApplications getInstalledApplications() throws PlayOnLinuxException {
        return mainEventHandler.getInstalledApplications();
    }

    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxException {
        return mainEventHandler.getInstalledVirtualDrives();
    }

    public void runLocalScript(File scriptToRun) throws IOException {
        mainEventHandler.runLocalScript(scriptToRun);
    }

    public PlayOnLinuxWindow openConfigureWindow(PlayOnLinuxWindow parent, String selectedApplication)
            throws PlayOnLinuxException {
        return ConfigureWindow.getInstance(parent);
    }

    public PlayOnLinuxWindow openInstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxException {
        return InstallWindow.getInstance(parent);
    }

    public RemoteAvailableInstallers getRemoteAvailableInstallers() throws PlayOnLinuxException {
        return mainEventHandler.getRemoteAvailableInstallers();
    }

    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }

    public void onApplicationStarted() throws MalformedURLException {
        mainEventHandler.onApplicationStarted();
    }


}
