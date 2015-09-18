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
import com.playonlinux.core.gpg.SignatureChecker;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.scripts.InstallerSource;
import com.playonlinux.core.scripts.InstallerSourceWebserviceDefaultImplementation;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.core.webservice.HTTPDownloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

@Scan
public class DefaultAppsManager extends ObservableDefaultImplementation<DefaultAppsManager> implements AppsManager {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager serviceManager;

    private InstallerSourceWebserviceDefaultImplementation installerSourceWebserviceImplementation;
    private URL webserviceUrl;
    private DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope;

    @Override
    public void update(InstallerSource installerSource, DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope) {
        this.downloadEnvelope = downloadEnvelope;
        this.notifyObservers(this);
    }

    @Override
    public void refresh() throws ServiceInitializationException {
        if (installerSourceWebserviceImplementation != null) {
            installerSourceWebserviceImplementation.deleteObserver(this);
            serviceManager.unregister(installerSourceWebserviceImplementation);
        }
        installerSourceWebserviceImplementation = new InstallerSourceWebserviceDefaultImplementation(webserviceUrl);
        installerSourceWebserviceImplementation.addObserver(this);
        serviceManager.register(installerSourceWebserviceImplementation);
    }

    @Override
    public InstallerDownloaderEntityProvider getDownloaderEntityProvider(String scriptUrl) throws AppsManagerException {
        try {
            return getDownloaderEntityProvider(new URL(scriptUrl));
        } catch (MalformedURLException e) {
            throw new AppsManagerException("The URL was bad formed", e);
        }
    }

    @Override
    public InstallerDownloaderEntityProvider getDownloaderEntityProvider(URL scriptUrl) {
        return new DefaultInstallerDownloaderEntityProvider(new HTTPDownloader(scriptUrl), new SignatureChecker());
    }

    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
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
}
