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

package com.playonlinux.app;

import java.util.Arrays;
import java.util.Iterator;

import com.playonlinux.apps.AppsEntitiesProvider;
import com.playonlinux.apps.AppsManager;
import com.playonlinux.containers.ContainersEntitiesProvider;
import com.playonlinux.containers.ContainersManager;
import com.playonlinux.containers.DefaultContainersManager;
import com.playonlinux.core.services.manager.ServiceImplementationDefinition;
import com.playonlinux.core.services.manager.ServiceManagerConfiguration;
import com.playonlinux.core.webservice.DownloadManager;
import com.playonlinux.engines.wine.DefaultWineVersionsManager;
import com.playonlinux.engines.wine.WineVersionEntitiesProvider;
import com.playonlinux.engines.wine.WineVersionManager;
import com.playonlinux.library.LibraryEntitiesProvider;

/**
 * Default service configuration of PlayOnLinux
 * The order the services are declared matters
 */
class PlayOnLinuxServicesConfiguration implements ServiceManagerConfiguration {
    @Override
    public Iterator<ServiceImplementationDefinition> iterator() {
        return Arrays.asList(
                new ServiceImplementationDefinition(DownloadManager.class, DownloadManager.class),
                new ServiceImplementationDefinition(AppsManager.class, AppsManager.class),
                new ServiceImplementationDefinition(AppsEntitiesProvider.class, AppsEntitiesProvider.class),
                new ServiceImplementationDefinition(WineVersionManager.class, DefaultWineVersionsManager.class),
                new ServiceImplementationDefinition(WineVersionEntitiesProvider.class, WineVersionEntitiesProvider.class),
                new ServiceImplementationDefinition(LibraryEntitiesProvider.class, LibraryEntitiesProvider.class),
                new ServiceImplementationDefinition(ContainersManager.class, DefaultContainersManager.class),
                new ServiceImplementationDefinition(ContainersEntitiesProvider.class, ContainersEntitiesProvider.class)
        ).iterator();
    }
}
