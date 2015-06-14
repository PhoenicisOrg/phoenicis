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

package com.playonlinux.services;

import com.playonlinux.utils.Progressable;
import com.playonlinux.domain.PlayOnLinuxException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public interface EventHandler {
    void runLocalScript(File scriptToRun) throws IOException;

    InstalledApplications getInstalledApplications() throws PlayOnLinuxException;

    InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxException;

    RemoteAvailableInstallers getRemoteAvailableInstallers();

    void onApplicationStarted() throws MalformedURLException;

    Progressable getRemoteInstallerDownloaderDownloader();

    void installProgram(String selectedItemLabel);

    void runApplication(String applicationName) throws PlayOnLinuxException;
}
