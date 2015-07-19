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

package com.playonlinux.engines.wine;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.dto.ui.ProgressStateDTO;
import com.playonlinux.dto.web.WineVersionDistributionDTO;
import com.playonlinux.core.services.manager.AutoStartedService;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.webservice.DownloadEnvelope;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@Scan
@AutoStartedService(type = WineVersionsManager.class)
public class WineVersionsManager
        extends AbstractObservableImplementation<WineVersionsManager>
        implements Observer<Observable, Object>, Service {

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;


    private Collection<WineVersionDistributionDTO> wineVersionDistributionDTOs = new ArrayList<>();
    private DownloadEnvelope<Collection<WineVersionDistributionDTO>> downloadEnvelope;

    private WineversionsSourceWebserviceImplementation wineversionsSourceWebserviceImplementation;
    private URL webserviceUrl;

    @Override
    public void update(Observable observable, Object argument) {
        /*
         * Because of some limitations of Java's Generics we need to do this way.
         */
        // TODO: Observe the local directory
        if(argument instanceof DownloadEnvelope && observable instanceof WineVersionSource) {
            this.downloadEnvelope = (DownloadEnvelope<Collection<WineVersionDistributionDTO>>) argument;
            if(downloadEnvelope.getEnvelopeContent() != null) {
                this.wineVersionDistributionDTOs = downloadEnvelope.getEnvelopeContent();
            }
            notifyObservers(this);
        }
    }

    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
    public void start() throws ServiceInitializationException {
        try {
            webserviceUrl = playOnLinuxContext.makeWineVersionWebserviceUrl();
        } catch (MalformedURLException e) {
            throw new ServiceInitializationException(e);
        }
        this.refreshWebservice();
    }

    public boolean isUpdating() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateDTO.State.PROGRESSING;
    }

    public boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateDTO.State.FAILED;
    }

    private void refreshWebservice() throws ServiceInitializationException {
        if(wineversionsSourceWebserviceImplementation != null) {
            wineversionsSourceWebserviceImplementation.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(wineversionsSourceWebserviceImplementation);
        }
        wineversionsSourceWebserviceImplementation = new WineversionsSourceWebserviceImplementation(webserviceUrl);
        wineversionsSourceWebserviceImplementation.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(wineversionsSourceWebserviceImplementation);
    }

    public Collection<WineVersionDistributionDTO> getWineVersionDistributionDTOs() {
        return wineVersionDistributionDTOs;
    }
}
