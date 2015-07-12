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

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.installer.Script;
import com.playonlinux.installer.ScriptFactory;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;

import com.playonlinux.services.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Scan
public final class EventDispatcherPlayOnLinuxImplementation implements EventDispatcher {
    private static final String INSTALLED_APPLICATION_SERVICE_KEY =
            "com.playonlinux.services.InstalledApplicationsPlayOnLinuxImplementation";
    private static final String REMOTE_INSTALER_SERVICE_KEY =
            "com.playonlinux.services.RemoteAvailableInstallersPlayOnLinuxImplementation";

    @Inject
    static BackgroundServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;
    
    @Inject
    private static ScriptFactory scriptFactory;

    private static final Logger LOGGER = Logger.getLogger(EventDispatcherPlayOnLinuxImplementation.class);

    private InstalledVirtualDrivesPlayOnLinuxImplementation virtualDrives;
    private RemoteAvailableInstallers remoteAvailableInstallers;

    @Override
    public void runLocalScript(File scriptToRun) throws PlayOnLinuxException {
        Script playonlinuxScript = scriptFactory.createInstance(scriptToRun);
        playOnLinuxBackgroundServicesManager.register(playonlinuxScript);
    }

    @Override
    public void runApplication(String applicationName) throws PlayOnLinuxException {
        Script playonLinuxScript = scriptFactory.createInstance(
                new File(playOnLinuxContext.makeShortcutsScriptsPath(), applicationName)
        );
        playOnLinuxBackgroundServicesManager.register(playonLinuxScript);
    }

    @Override
    public InstalledApplications getInstalledApplications() throws PlayOnLinuxException {
        if(!playOnLinuxBackgroundServicesManager.containsService(INSTALLED_APPLICATION_SERVICE_KEY)) {
            playOnLinuxBackgroundServicesManager.register(INSTALLED_APPLICATION_SERVICE_KEY,
                    new InstalledApplicationsPlayOnLinuxImplementation());
        }

        return playOnLinuxBackgroundServicesManager.getBackgroundService(INSTALLED_APPLICATION_SERVICE_KEY
                , InstalledApplications.class);
    }

    @Override
    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxException {
        if(virtualDrives == null) {
            virtualDrives = new InstalledVirtualDrivesPlayOnLinuxImplementation();
        }

        return this.virtualDrives;
    }


    @Override
    public RemoteAvailableInstallers getRemoteAvailableInstallers() throws PlayOnLinuxException {
        if(!playOnLinuxBackgroundServicesManager.containsService(REMOTE_INSTALER_SERVICE_KEY)) {
            playOnLinuxBackgroundServicesManager.register(REMOTE_INSTALER_SERVICE_KEY,
                    new RemoteAvailableInstallersPlayOnLinuxImplementation());
        }

        return playOnLinuxBackgroundServicesManager.getBackgroundService(REMOTE_INSTALER_SERVICE_KEY
                , RemoteAvailableInstallers.class);
    }

    @Override
    public void onApplicationStarted() throws MalformedURLException {
        // Will be implemented later
    }



}
