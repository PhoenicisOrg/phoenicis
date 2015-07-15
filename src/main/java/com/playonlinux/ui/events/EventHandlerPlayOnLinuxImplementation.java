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

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.dto.ui.VirtualDriveDTO;
import com.playonlinux.dto.ui.engines.WineVersionDistributionItemDTO;
import com.playonlinux.dto.ui.engines.WineVersionsWindowDTO;
import com.playonlinux.dto.ui.library.InstalledApplicationDTO;
import com.playonlinux.dto.ui.library.LibraryWindowDTO;
import com.playonlinux.engines.wine.WineVersionEntitiesProvider;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.scripts.Script;
import com.playonlinux.core.scripts.ScriptFactory;
import com.playonlinux.apps.AvailableInstallersEntitiesProvider;
import com.playonlinux.library.InstalledApplicationsEntitiesProvider;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.services.virtualdrives.InstalledVirtualDrivesPlayOnLinuxImplementation;
import com.playonlinux.ui.api.EntitiesProvider;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;

@Scan
public final class EventHandlerPlayOnLinuxImplementation implements EventHandler {
    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static ScriptFactory scriptFactory;

    private InstalledVirtualDrivesPlayOnLinuxImplementation virtualDrives;

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
    public Iterable<VirtualDriveDTO> getInstalledVirtualDrives() throws PlayOnLinuxException {
        if(virtualDrives == null) {
            virtualDrives = new InstalledVirtualDrivesPlayOnLinuxImplementation();
        }

        return this.virtualDrives;
    }


    @Override
    public AvailableInstallersEntitiesProvider getRemoteAvailableInstallers() {
        return playOnLinuxBackgroundServicesManager.getBackgroundService(AvailableInstallersEntitiesProvider.class);
    }

    @Override
    public EntitiesProvider<WineVersionDistributionItemDTO, WineVersionsWindowDTO> getRemoteWineVersions() {
        return playOnLinuxBackgroundServicesManager.getBackgroundService(WineVersionEntitiesProvider.class);
    }

    @Override
    public EntitiesProvider<InstalledApplicationDTO, LibraryWindowDTO> getInstalledApplications() {
        return playOnLinuxBackgroundServicesManager.getBackgroundService(InstalledApplicationsEntitiesProvider.class);
    }

    @Override
    public void refreshAvailableInstallers() throws PlayOnLinuxException {
        getRemoteAvailableInstallers().refresh();
    }

    @Override
    public void onApplicationStarted() throws MalformedURLException {
        // Will be implemented later
    }



}
