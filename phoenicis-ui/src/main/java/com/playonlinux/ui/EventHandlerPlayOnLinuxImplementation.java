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

package com.playonlinux.ui;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.apps.AppsEntitiesProvider;
import com.playonlinux.apps.AppsManager;
import com.playonlinux.apps.InstallerDownloaderEntityProvider;
import com.playonlinux.containers.ContainersEntitiesProvider;
import com.playonlinux.containers.entities.ContainerEntity;
import com.playonlinux.containers.entities.ContainersWindowEntity;
import com.playonlinux.core.scripts.AnyScriptFactory;
import com.playonlinux.core.scripts.Script;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.engines.wine.WineVersionEntitiesProvider;
import com.playonlinux.engines.wine.entities.WineVersionDistributionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionsWindowEntity;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.library.LibraryEntitiesProvider;
import com.playonlinux.library.LibraryEventHandler;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;

@Scan
public final class EventHandlerPlayOnLinuxImplementation implements EventHandler {
    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static ObjectMapper objectMapper;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static AnyScriptFactory scriptFactory;

    private final LibraryEventHandler libraryEventHandler = new LibraryEventHandler();

    @Override
    public void runLocalScript(File scriptToRun) throws PlayOnLinuxException {
        Script playonlinuxScript = scriptFactory.createInstanceFromFile(scriptToRun);
        playOnLinuxBackgroundServicesManager.register(playonlinuxScript);
    }

    @Override
    public AppsManager getAppsManager() {
        return playOnLinuxBackgroundServicesManager.getService(AppsManager.class);
    }

    @Override
    public AppsEntitiesProvider getRemoteAvailableInstallers() {
        return playOnLinuxBackgroundServicesManager.getService(AppsEntitiesProvider.class);
    }

    @Override
    public EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> getRemoteWineVersions() {
        return playOnLinuxBackgroundServicesManager.getService(WineVersionEntitiesProvider.class);
    }

    @Override
    public EntitiesProvider<ContainerEntity, ContainersWindowEntity> getContainers() {
        return playOnLinuxBackgroundServicesManager.getService(ContainersEntitiesProvider.class);
    }

    @Override
    public EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> getInstalledApplications() {
        return playOnLinuxBackgroundServicesManager.getService(LibraryEntitiesProvider.class);
    }

    @Override
    public void refreshAvailableInstallers() throws PlayOnLinuxException {
        getAppsManager().refresh();
    }

    @Override
    public LibraryEventHandler getLibraryEventHandler() {
        return libraryEventHandler;
    }

    @Override
    public InstallerDownloaderEntityProvider getInstallerDownloaderEntityProvider(String scriptUrl) {
        return getAppsManager().getDownloaderEntityProvider(scriptUrl);
    }

    @Override
    public void onApplicationStarted() {
        // Will be implemented later
    }



}
