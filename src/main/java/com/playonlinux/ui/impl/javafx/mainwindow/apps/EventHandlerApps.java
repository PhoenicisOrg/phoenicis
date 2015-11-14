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

package com.playonlinux.ui.impl.javafx.mainwindow.apps;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.apps.AppsManagerException;
import com.playonlinux.apps.InstallerDownloaderEntityProvider;
import com.playonlinux.apps.entities.AppEntity;
import com.playonlinux.apps.entities.AppsWindowEntity;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.ui.api.UIEventHandler;
import com.playonlinux.ui.events.EventHandler;

@Scan
final class EventHandlerApps implements UIEventHandler {
    @Inject
    static EventHandler mainEventHandler;

    @Inject
    static ServiceManager serviceManager;

    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }

    public EntitiesProvider<AppEntity, AppsWindowEntity> getRemoteAvailableInstallers() {
        return mainEventHandler.getRemoteAvailableInstallers();
    }

    public void updateAvailableInstallers() throws PlayOnLinuxException {
        mainEventHandler.refreshAvailableInstallers();
    }

    public InstallerDownloaderEntityProvider getInstallerDownloaderEntityProvider(String scriptId)
            throws AppsManagerException {
        return mainEventHandler.getInstallerDownloaderEntityProvider(scriptId);
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }
}
