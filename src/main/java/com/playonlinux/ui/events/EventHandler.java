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

package com.playonlinux.ui.events;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.apps.AppsManager;
import com.playonlinux.apps.AppsManagerException;
import com.playonlinux.apps.InstallerDownloaderEntityProvider;
import com.playonlinux.apps.entities.AppEntity;
import com.playonlinux.apps.entities.AppsWindowEntity;
import com.playonlinux.containers.entities.ContainerEntity;
import com.playonlinux.containers.entities.ContainersWindowEntity;
import com.playonlinux.engines.wine.entities.WineVersionDistributionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionsWindowEntity;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;

import java.io.File;

/**
 * Call version models from the UI
 */
public interface EventHandler {
    /**
     * Run a local script
     * @param scriptToRun script to run
     * @throws PlayOnLinuxException if any error occurs
     */
    void runLocalScript(File scriptToRun) throws PlayOnLinuxException;

    /**
     * Get installed applications
     * @return installed applications observable
     */
    EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> getInstalledApplications();

    /**
     * Get the Available Apps Manager
     * @return The actual component
     */
    AppsManager getAppsManager();

    EntitiesProvider<AppEntity, AppsWindowEntity> getRemoteAvailableInstallers();

    EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> getRemoteWineVersions();

    EntitiesProvider<ContainerEntity, ContainersWindowEntity> getContainers();

    InstallerDownloaderEntityProvider getInstallerDownloaderEntityProvider(String scriptUrl) throws AppsManagerException;

    void onApplicationStarted();

    void runApplication(String applicationName) throws PlayOnLinuxException;

    void refreshAvailableInstallers() throws PlayOnLinuxException;

}
