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

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.entities.ProgressState;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.utils.ChecksumCalculator;
import com.playonlinux.core.utils.Files;
import com.playonlinux.core.utils.archive.Extractor;
import com.playonlinux.core.version.Version;
import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.core.webservice.HTTPDownloader;
import com.playonlinux.engines.wine.dto.WineVersionDTO;
import com.playonlinux.engines.wine.dto.WineVersionDistributionWebDTO;
import com.playonlinux.engines.wine.packages.GeckoWinePackage;
import com.playonlinux.engines.wine.packages.MonoWinePackage;
import com.playonlinux.engines.wine.packages.WinePackageProvider;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.ProgressControl;

import lombok.Getter;
import lombok.Setter;

@Scan
public class DefaultWineVersionsManager implements WineVersionManager {
    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Getter
    private Collection<WineVersionDistributionWebDTO> wineVersionDistributionDTOs = new ArrayList<>();
    private DownloadEnvelope<Collection<WineVersionDistributionWebDTO>> downloadEnvelope;

    private WineversionsSourceWebserviceDefaultImplementation wineversionsSourceWebserviceImplementation;
    private URL webserviceUrl;
    @Setter
    private Consumer<WineVersionManager> onChange;

    public synchronized void update(DownloadEnvelope<Collection<WineVersionDistributionWebDTO>> argument) {
        /*
         * Because of some limitations of Java's Generics we need to do this
         * way.
         */
        // TODO: Observe the local directory
        this.downloadEnvelope = argument;

        if (downloadEnvelope.getEnvelopeContent() != null) {
            this.wineVersionDistributionDTOs = downloadEnvelope.getEnvelopeContent();
        }

        if (onChange != null) {
            onChange.accept(this);
        }
    }

    @Override
    public void shutdown() {
        // Nothing to do
    }

    @Override
    public void init() {
        try {
            webserviceUrl = playOnLinuxContext.makeWineVersionWebserviceUrl();
        } catch (MalformedURLException e) {
            throw new ServiceInitializationException(e);
        }
        this.refreshWebservice();
    }

    @Override
    public boolean isUpdating() {
        return downloadEnvelope == null || downloadEnvelope.getDownloadState().getState() == ProgressState.PROGRESSING;
    }

    @Override
    public boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressState.FAILED;
    }

    private synchronized void refreshWebservice() {
        if (wineversionsSourceWebserviceImplementation != null) {

            playOnLinuxBackgroundServicesManager.unregister(wineversionsSourceWebserviceImplementation);
        }
        wineversionsSourceWebserviceImplementation = new WineversionsSourceWebserviceDefaultImplementation(
                webserviceUrl);
        wineversionsSourceWebserviceImplementation.setOnDownloadUpdate(this::update);
        playOnLinuxBackgroundServicesManager.register(wineversionsSourceWebserviceImplementation);
    }

    @Override
    public void install(WineDistribution wineDistribution, Version version, ProgressControl progressControl) {
        final WineVersionDTO wineVersionDTO = getWineVersionFromDistributionAndVersion(wineDistribution, version);
        final URL packageUrl = this.makePackageUrl(wineVersionDTO);
        final String serverSum = wineVersionDTO.getSha1sum();

        final HTTPDownloader httpDownloader = new HTTPDownloader(packageUrl);

        httpDownloader.setOnChange(progressControl);
        try {
            final File temporaryFile = File.createTempFile("wineVersion", "tar");
            temporaryFile.deleteOnExit();
            httpDownloader.get(temporaryFile);

            final ChecksumCalculator checksumCalculator = new ChecksumCalculator();
            checksumCalculator.setOnChange(progressControl);
            final String clientSum = checksumCalculator.calculate(temporaryFile, "sha1");
            if (!clientSum.equals(serverSum)) {
                throw new EngineInstallException(String.format(
                        "Error while downloading the file. Hash mismatch." + System.lineSeparator()
                                + System.lineSeparator() + "Client hash:%s" + System.lineSeparator() + "Server has:%s",
                        clientSum, serverSum));
            }

            install(wineDistribution, version, temporaryFile, progressControl);
            new WinePackageProvider<>(new GeckoWinePackage(wineVersionDTO))
                    .installPackageForWineVersion(getExtractPath(wineDistribution, version), progressControl);
            new WinePackageProvider<>(new MonoWinePackage(wineVersionDTO))
                    .installPackageForWineVersion(getExtractPath(wineDistribution, version), progressControl);

        } catch (IOException e) {
            throw new EngineInstallException(format("An error occurred while trying to download %s", packageUrl), e);
        }
    }

    @Override
    public void uninstall(WineDistribution wineDistribution, Version version, ProgressControl progressControl) {
        try {
            Files.remove(getExtractPath(wineDistribution, version));
        } catch (IOException e) {
            throw new EngineInstallException(
                    format("An error occurred while removing wine %s %s", version, wineDistribution), e);
        }
    }

    @Override
    public void install(WineDistribution wineDistribution, Version version, File localFile,
            ProgressControl progressControl) {
        final File extractPath = getExtractPath(wineDistribution, version);
        final Extractor extractor = new Extractor();
        extractor.setOnChange(progressControl);
        extractor.uncompress(localFile, extractPath);
    }

    private File getExtractPath(WineDistribution wineDistribution, Version version) {
        final File wineResources = playOnLinuxContext.makeEnginesPath("wine");
        return new File(wineResources,
                format("%s/%s", wineDistribution.asNameWithCurrentOperatingSystem(), version.toString()));
    }

    private synchronized WineVersionDTO getWineVersionFromDistributionAndVersion(WineDistribution wineDistribution,
            Version version) {
        final String coordinateName = wineDistribution.asNameWithCurrentOperatingSystem();

        for (WineVersionDistributionWebDTO wineVersionDistributionWebDTO : wineVersionDistributionDTOs) {
            if (coordinateName.equals(wineVersionDistributionWebDTO.getName())) {
                for (WineVersionDTO wineVersionDTO : wineVersionDistributionWebDTO.getPackages()) {
                    if (version.toString().equals(wineVersionDTO.getVersion())) {
                        return wineVersionDTO;
                    }
                }
            }
        }

        throw new EngineInstallException(
                format("The version you are trying to install (%s / %s), does not seem to exists. (Codename: %s)",
                        wineDistribution.toString(), version.toString(), coordinateName));
    }

    private synchronized URL makePackageUrl(WineVersionDTO wineVersionDTO) {
        try {
            return new URL(wineVersionDTO.getUrl());
        } catch (MalformedURLException e) {
            throw new EngineInstallException("Malformed URL in PlayOnLinux webservice. Please report the error", e);
        }
    }
}