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

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.domain.*;
import com.playonlinux.installer.Script;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.installer.ScriptFactory;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Scan
public class EventDispatcherPlayOnLinuxImplementation implements EventDispatcher {
    @Inject
    static BackgroundServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private Logger logger = Logger.getLogger(this.getClass());


    private InstalledApplications installedApplications;
    private InstalledVirtualDrivesPlayOnLinuxImplementation virtualDrives;
    private RemoteAvailableInstallers remoteAvailableInstallers;

    @Override
    public void runLocalScript(File scriptToRun) throws IOException {
        Script playonlinuxScript = ScriptFactory.createInstance(scriptToRun);
        playOnLinuxBackgroundServicesManager.register(playonlinuxScript);
    }

    @Override
    public void runApplication(String applicationName) throws PlayOnLinuxException {
        try {
            Script playonLinuxScript = ScriptFactory.createInstance(new File(playOnLinuxContext.makeShortcutsScriptsPath(), applicationName));
            playOnLinuxBackgroundServicesManager.register(playonLinuxScript);
        } catch (IOException e) {
            throw new PlayOnLinuxException("Unable to run this script", e);
        }
    }

    @Override
    public InstalledApplications getInstalledApplications() throws PlayOnLinuxException {
        if(installedApplications == null) {
            installedApplications = new InstalledApplicationsPlayOnLinuxImplementation();
        }
        return this.installedApplications;
    }

    @Override
    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxException {
        if(virtualDrives == null) {
            virtualDrives = new InstalledVirtualDrivesPlayOnLinuxImplementation();
        }

        return this.virtualDrives;
    }


    @Override
    public RemoteAvailableInstallers getRemoteAvailableInstallers() {
        if(remoteAvailableInstallers == null) {
            try {
                remoteAvailableInstallers = new RemoteAvailableInstallersPlayOnLinuxImplementation();
            } catch (MalformedURLException e) {
                logger.error("The URL was malformed", e);
            }
        }

        return this.remoteAvailableInstallers;
    }

    @Override
    public void onApplicationStarted() throws MalformedURLException {
        // Will be implemented later
    }



}
