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

import java.io.File;

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
import com.playonlinux.library.LibraryEventHandler;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;

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

    /**
     * Remote available installer entity provider
     * @return the entity provider
     */
    EntitiesProvider<AppEntity, AppsWindowEntity> getRemoteAvailableInstallers();

    /**
     * WineVersion entity provider
     * @return the entity provider
     */
    EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> getRemoteWineVersions();

    /**
     * Containers entity provider
     * @return the entity provider
     */
    EntitiesProvider<ContainerEntity, ContainersWindowEntity> getContainers();

    LibraryEventHandler getLibraryEventHandler();

    /**
     * Get a entity provider to download a script
     * @param scriptUrl The script URL as a string
     * @return the entity provider
     * @throws AppsManagerException if the URL is malformed
     */
    InstallerDownloaderEntityProvider getInstallerDownloaderEntityProvider(String scriptUrl) throws AppsManagerException;

    /**
     * Events to be run when the application is started
     */
    void onApplicationStarted();

    /**
     * Refresh the lsit of available installers
     * @throws PlayOnLinuxException If any error occur
     */
    void refreshAvailableInstallers() throws PlayOnLinuxException;
}
