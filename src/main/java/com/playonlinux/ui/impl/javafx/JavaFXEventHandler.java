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

package com.playonlinux.ui.impl.javafx;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.api.InstalledApplications;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.ui.impl.configurewindow.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.configurewindow.ConfigureWindow;
import com.playonlinux.ui.impl.javafx.installwindow.InstallWindow;

import java.io.File;
import java.io.IOException;

@Component
public class JavaFXEventHandler {
    @Inject
    static EventHandler mainEventHandler;


    public InstalledApplications getInstalledApplications() throws PlayOnLinuxError {
        return mainEventHandler.getInstalledApplications();
    }

    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxError {
        return mainEventHandler.getInstalledVirtualDrives();
    }

    public void runLocalScript(File scriptToRun) throws IOException {
        mainEventHandler.runLocalScript(scriptToRun);
    }

    public PlayOnLinuxWindow openConfigureWindow(PlayOnLinuxWindow parent, String selectedApplication)
            throws PlayOnLinuxError {
        return ConfigureWindow.getInstance(parent);
    }

    public PlayOnLinuxWindow openInstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        return InstallWindow.getInstance(parent);
    }
}
