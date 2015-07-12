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

package com.playonlinux.events;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.dto.ui.apps.AppsItemDTO;
import com.playonlinux.dto.ui.apps.AppsWindowDTO;
import com.playonlinux.dto.ui.library.InstalledApplicationDTO;
import com.playonlinux.dto.ui.VirtualDriveDTO;
import com.playonlinux.dto.ui.library.LibraryWindowDTO;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.utils.filter.Filterable;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Call version models from the UI
 */
public interface EventDispatcher {
    /**
     * Run a local script
     * @param scriptToRun script to run
     * @throws PlayOnLinuxException if any error occurs
     */
    void runLocalScript(File scriptToRun) throws PlayOnLinuxException;

    /**
     * Get installed applications
     * @return installed applications observable
     * @throws PlayOnLinuxException
     */
    EntitiesProvider<InstalledApplicationDTO, LibraryWindowDTO> getInstalledApplications();

    Iterable<VirtualDriveDTO> getInstalledVirtualDrives() throws PlayOnLinuxException;

    EntitiesProvider<AppsItemDTO, AppsWindowDTO> getRemoteAvailableInstallers();

    void onApplicationStarted() throws MalformedURLException;

    void runApplication(String applicationName) throws PlayOnLinuxException;

    void refreshAvailableInstallers() throws PlayOnLinuxException;
}
