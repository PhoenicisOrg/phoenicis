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

package com.playonlinux.apps;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.core.entities.ProgressState;
import com.playonlinux.core.gpg.SignatureChecker;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.core.webservice.HTTPDownloader;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

@Scan
public class AppsManager implements Service {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager serviceManager;

    private Runnable onChange;

    private InstallerSourceWebserviceDefaultImplementation installerSourceWebserviceImplementation;
    private URL webserviceUrl;
    private DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope;

    public boolean isUpdating() {
        return downloadEnvelope == null || downloadEnvelope.getDownloadState().getState() == ProgressState.PROGRESSING;
    }

    public boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressState.FAILED;
    }

    public void update(DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope) {
        this.downloadEnvelope = downloadEnvelope;
        if(onChange != null){
            onChange.run();
        }
    }

    public void refresh() throws ServiceInitializationException {
        if (installerSourceWebserviceImplementation != null) {
            serviceManager.unregister(installerSourceWebserviceImplementation);
        }
        installerSourceWebserviceImplementation = new InstallerSourceWebserviceDefaultImplementation(webserviceUrl);
        installerSourceWebserviceImplementation.setOnDownloadUpdate(this::update);
        serviceManager.register(installerSourceWebserviceImplementation);
    }

    public InstallerDownloaderEntityProvider getDownloaderEntityProvider(String scriptUrl) {
        try {
            return getDownloaderEntityProvider(new URL(scriptUrl));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("The URL was bad formed", e);
        }
    }

    public InstallerDownloaderEntityProvider getDownloaderEntityProvider(URL scriptUrl) {
        return new DefaultInstallerDownloaderEntityProvider(new HTTPDownloader(scriptUrl), new SignatureChecker());
    }

    public void shutdown() {
        // Nothing to shutdown
    }

    public void init() throws ServiceInitializationException {
        try {
            webserviceUrl = playOnLinuxContext.makeInstallersWebserviceUrl();
        } catch (MalformedURLException e) {
            throw new ServiceInitializationException(e);
        }
        this.refresh();
    }

    public DownloadEnvelope<Collection<CategoryDTO>> getDownloadEnvelope() {
        return downloadEnvelope;
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }
}
