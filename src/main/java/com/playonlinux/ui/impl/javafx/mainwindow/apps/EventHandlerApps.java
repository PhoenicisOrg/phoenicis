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
import com.playonlinux.dto.ui.AppsItemDTO;
import com.playonlinux.events.EventDispatcher;
import com.playonlinux.services.availableapplications.RemoteAvailableInstallers;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.UIEventHandler;


@Scan
final class EventHandlerApps implements UIEventHandler {
    @Inject
    static EventDispatcher mainEventDispatcher;

    @Override
    public EventDispatcher getMainEventHandler() {
        return mainEventDispatcher;
    }

    public RemoteAvailableInstallers getRemoteAvailableInstallers() throws PlayOnLinuxException {
        return mainEventDispatcher.getRemoteAvailableInstallers();
    }

    public void updateAvailableInstallers() throws PlayOnLinuxException {
        getRemoteAvailableInstallers().refresh();
    }

    public void installApp(AppsItemDTO appsItemDTO, int scriptId) {
        System.out.println("Will handle installation of script "+scriptId);
    }
}
