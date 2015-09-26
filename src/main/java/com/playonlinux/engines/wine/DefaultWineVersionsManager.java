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
import com.playonlinux.core.entities.ProgressStateEntity;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.utils.ChecksumCalculator;
import com.playonlinux.core.utils.Files;
import com.playonlinux.core.utils.archive.ArchiveException;
import com.playonlinux.core.utils.archive.Extractor;
import com.playonlinux.core.version.Version;
import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.core.webservice.DownloadException;
import com.playonlinux.core.webservice.HTTPDownloader;
import com.playonlinux.engines.wine.dto.WineVersionDistributionWebDTO;
import com.playonlinux.engines.wine.dto.WineVersionWebDTO;
import com.playonlinux.ui.api.ProgressControl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;

@Scan
public class DefaultWineVersionsManager
        extends ObservableDefaultImplementation<WineVersionManager>
        implements WineVersionManager {

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;


    private Collection<WineVersionDistributionWebDTO> wineVersionDistributionDTOs = new ArrayList<>();
    private DownloadEnvelope<Collection<WineVersionDistributionWebDTO>> downloadEnvelope;

    private WineversionsSourceWebserviceDefaultImplementation wineversionsSourceWebserviceImplementation;
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
    public void init() throws ServiceInitializationException {
        try {
            webserviceUrl = playOnLinuxContext.makeWineVersionWebserviceUrl();
        } catch (MalformedURLException e) {
            throw new ServiceInitializationException(e);
        }
        this.refreshWebservice();
    }

    public boolean isUpdating() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateEntity.State.PROGRESSING;
    }

    public boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateEntity.State.FAILED;
    }

    private synchronized void refreshWebservice() throws ServiceInitializationException {
        if(wineversionsSourceWebserviceImplementation != null) {
            wineversionsSourceWebserviceImplementation.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(wineversionsSourceWebserviceImplementation);
        }
        wineversionsSourceWebserviceImplementation = new WineversionsSourceWebserviceDefaultImplementation(webserviceUrl);
        wineversionsSourceWebserviceImplementation.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(wineversionsSourceWebserviceImplementation);
    }

    public Collection<WineVersionDistributionWebDTO> getWineVersionDistributionDTOs() {
        return wineVersionDistributionDTOs;
    }

    @Override
    public void install(WineDistribution wineDistribution, Version version, ProgressControl progressControl) throws EngineInstallException {
        final URL packageUrl = this.makePackageUrl(wineDistribution, version);
        final String serverSum = this.makeSha1Sum(wineDistribution, version);

        final HTTPDownloader httpDownloader = new HTTPDownloader(packageUrl);

        httpDownloader.addObserver(progressControl);
        try {
            final File temporaryFile = File.createTempFile("wineVersion", "tar");
            temporaryFile.deleteOnExit();
            httpDownloader.get(temporaryFile);
            httpDownloader.deleteObservers();

            final ChecksumCalculator checksumCalculator = new ChecksumCalculator();
            checksumCalculator.addObserver(progressControl);
            final String clientSum = checksumCalculator.calculate(temporaryFile, "sha1");
            checksumCalculator.deleteObservers();
            if(!clientSum.equals(serverSum)) {
                throw new EngineInstallException(String.format("Error while downloading the file. Hash mismatch." +
                        System.lineSeparator() + System.lineSeparator() +
                        "Client hash:%s" + System.lineSeparator() +
                        "Server has:%s", clientSum, serverSum));
            }

            install(wineDistribution, version, temporaryFile, progressControl);

        } catch (IOException | DownloadException e) {
            throw new EngineInstallException(format("An error occurred while trying to download %s", packageUrl), e);
        }
    }

    @Override
    public void uninstall(WineDistribution wineDistribution, Version version, ProgressControl progressControl) throws EngineInstallException {
        try {
            Files.remove(getExtractPath(wineDistribution, version));
        } catch (IOException e) {
            throw new EngineInstallException(format("An error occurred while removing wine %s %s", version, wineDistribution), e);
        }
    }

    @Override
    public void install(WineDistribution wineDistribution, Version version, File localFile, ProgressControl progressControl) throws EngineInstallException {
        final File extractPath = getExtractPath(wineDistribution, version);

        final Extractor extractor = new Extractor();
        extractor.addObserver(progressControl);

        try {
            extractor.uncompress(localFile, extractPath);
        } catch (ArchiveException e) {
           throw new EngineInstallException("Unable to extract archive", e);
        } finally {
            extractor.deleteObservers();
        }


    }

    private File getExtractPath(WineDistribution wineDistribution, Version version) {
        final File wineResources = playOnLinuxContext.makeEnginesPath("wine");
        return new File(wineResources, format("%s/%s",
                wineDistribution.asNameWithCurrentOperatingSystem(),
                version.toString())
        );
    }

    private synchronized WineVersionWebDTO getWineVersionFromDistributionAndVersion(WineDistribution wineDistribution, Version version) throws EngineInstallException {
        final String coordinateName = wineDistribution.asNameWithCurrentOperatingSystem();

        for (WineVersionDistributionWebDTO wineVersionDistributionWebDTO : wineVersionDistributionDTOs) {
            if (coordinateName.equals(wineVersionDistributionWebDTO.getName())) {
                for (WineVersionWebDTO wineVersionWebDTO : wineVersionDistributionWebDTO.getPackages()) {
                    if (version.toString().equals(wineVersionWebDTO.getVersion())) {
                        return wineVersionWebDTO;
                    }
                }
            }
        }

        throw new EngineInstallException(format("The version you are trying to install (%s / %s), does not seem to exists. (Codename: %s)",
                wineDistribution.toString(), version.toString(), coordinateName));
    }

    private synchronized String makeSha1Sum(WineDistribution wineDistribution, Version version) throws EngineInstallException {
        return getWineVersionFromDistributionAndVersion(wineDistribution, version).getSha1sum();
    }

    private synchronized URL makePackageUrl(WineDistribution wineDistribution, Version version) throws EngineInstallException {
        try {
            return new URL(getWineVersionFromDistributionAndVersion(wineDistribution, version).getUrl());
        } catch(MalformedURLException e) {
            throw new EngineInstallException("Malformed URL in PlayOnLinux webservice. Please report the error", e);
        }
    }

}