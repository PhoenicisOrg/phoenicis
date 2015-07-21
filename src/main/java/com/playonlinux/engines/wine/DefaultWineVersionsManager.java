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
import com.playonlinux.core.webservice.DownloadException;
import com.playonlinux.core.webservice.HTTPDownloader;
import com.playonlinux.dto.ui.ProgressStateDTO;
import com.playonlinux.dto.web.WineVersionDistributionWebDTO;
import com.playonlinux.core.services.manager.AutoStartedService;

import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.core.observer.Observable;

import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.dto.web.WineVersionWebDTO;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.utils.archive.ArchiveException;
import com.playonlinux.utils.archive.TarExtractor;
import com.playonlinux.version.Version;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;

@Scan
@AutoStartedService(type = WineVersionManager.class)
public class DefaultWineVersionsManager
        extends AbstractObservableImplementation<WineVersionManager>
        implements WineVersionManager {

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;


    private Collection<WineVersionDistributionWebDTO> wineVersionDistributionDTOs = new ArrayList<>();
    private DownloadEnvelope<Collection<WineVersionDistributionWebDTO>> downloadEnvelope;

    private WineversionsSourceWebserviceImplementation wineversionsSourceWebserviceImplementation;
    private URL webserviceUrl;

    @Override
    public synchronized void update(Observable observable, Object argument) {
        /*
         * Because of some limitations of Java's Generics we need to do this way.
         */
        // TODO: Observe the local directory
        if(argument instanceof DownloadEnvelope && observable instanceof WineVersionSource) {
            this.downloadEnvelope = (DownloadEnvelope<Collection<WineVersionDistributionWebDTO>>) argument;
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

    private synchronized void refreshWebservice() throws ServiceInitializationException {
        if(wineversionsSourceWebserviceImplementation != null) {
            wineversionsSourceWebserviceImplementation.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(wineversionsSourceWebserviceImplementation);
        }
        wineversionsSourceWebserviceImplementation = new WineversionsSourceWebserviceImplementation(webserviceUrl);
        wineversionsSourceWebserviceImplementation.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(wineversionsSourceWebserviceImplementation);
    }

    public Collection<WineVersionDistributionWebDTO> getWineVersionDistributionDTOs() {
        return wineVersionDistributionDTOs;
    }

    @Override
    public void install(WineDistribution wineDistribution, Version version, ProgressControl progressControl) throws EngineInstallException {
        final URL packageUrl = this.makePackageUrl(wineDistribution, version);
        final HTTPDownloader httpDownloader = new HTTPDownloader(packageUrl);
        httpDownloader.addObserver(progressControl);
        try {
            final File temporaryFile = File.createTempFile("wineVersion", "tar");
            temporaryFile.deleteOnExit();
            httpDownloader.get(temporaryFile);
            install(wineDistribution, version, temporaryFile, progressControl);
            httpDownloader.deleteObservers();
        } catch (IOException | DownloadException e) {
            throw new EngineInstallException(format("An error occured while trying to download %s", packageUrl), e);
        }
    }

    @Override
    public void install(WineDistribution wineDistribution, Version version, File localFile, ProgressControl progressControl) throws EngineInstallException {
        final File wineResources = playOnLinuxContext.makeEnginesPath("wine");
        final File extractPath = new File(wineResources, format("%s/%s",
                wineDistribution.getDistributionCode(),
                version.toString())
        );

        final TarExtractor tarExtractor = new TarExtractor();
        tarExtractor.addObserver(progressControl);

        try {
            tarExtractor.uncompress(localFile, extractPath);
        } catch (ArchiveException e) {
           throw new EngineInstallException("Unable to extract archive", e);
        } finally {
            tarExtractor.deleteObservers();
        }


    }

    private synchronized URL makePackageUrl(WineDistribution wineDistribution, Version version) throws EngineInstallException {
        final String coordinateName = wineDistribution.asNameWithCurrentOperatingSystem();

        try {
            for (WineVersionDistributionWebDTO wineVersionDistributionWebDTO : wineVersionDistributionDTOs) {
                if (coordinateName.equals(wineVersionDistributionWebDTO.getName())) {
                    for (WineVersionWebDTO wineVersionWebDTO : wineVersionDistributionWebDTO.getPackages()) {
                        if (version.toString().equals(wineVersionWebDTO.getVersion())) {
                            return new URL(wineVersionWebDTO.getUrl());
                        }
                    }
                }
            }
        } catch(MalformedURLException e) {
            throw new EngineInstallException("Malformed URL in PlayOnLinux webservice. Please report the error", e);
        }

        throw new EngineInstallException(format("The version you are trying to install (%s / %s), does not seem to exists. (Codename: %s)",
                wineDistribution.toString(), version.toString(), coordinateName));
    }

}