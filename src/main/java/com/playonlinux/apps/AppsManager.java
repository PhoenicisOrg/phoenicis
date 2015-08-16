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

import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.scripts.InstallerSource;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.webservice.DownloadEnvelope;

import java.net.URL;
import java.util.Collection;

/**
 * Available application manager
 */
public interface AppsManager extends Observer<InstallerSource, DownloadEnvelope<Collection<CategoryDTO>>>,
                                     Observable<DefaultAppsManager>,
                                     Service {

    /**
     * Refresh the list of available application from the web service
     * @throws ServiceInitializationException
     */
    void refresh() throws ServiceInitializationException;

    /**
     * Returns an UI EntityProvider that will be observed while downloading a script
     * @param scriptUrl The url of the script to be downloaded as a string
     * @return The entity provider
     * @throws AppsManagerException if the URL was malformed
     */
    InstallerDownloaderEntityProvider getDownloaderEntityProvider(String scriptUrl) throws AppsManagerException;

    /**
     * Returns an UI EntityProvider that will be observed while downloading a script
     * @param scriptUrl The url of the script to be downloaded as a {@link URL} object
     * @return The entity provider
     */
    InstallerDownloaderEntityProvider getDownloaderEntityProvider(URL scriptUrl);
}
