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

package com.playonlinux.engines.wine.packages;

import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.utils.ChecksumCalculator;
import com.playonlinux.core.webservice.DownloadException;
import com.playonlinux.core.webservice.HTTPDownloader;
import com.playonlinux.engines.wine.EngineInstallException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

public class WinePackageProvider<T extends WinePackage> {
    private final T winePackage;

    public WinePackageProvider(T winePackage) {
        this.winePackage = winePackage;
    }

    public void installPackageForWineVersion(File extractPath, Observer progressControl) throws EngineInstallException {
        if(!winePackage.getPackageDestination().exists()) {
            installPackageInLocalCache(progressControl);
        }

        installPackageInWineVersionDirectory(extractPath);
    }

    private void installPackageInWineVersionDirectory(File extractPath) throws EngineInstallException {
        final File localArchive = winePackage.getPackageDestination();
        final File linkDestination = new File(extractPath, String.format("share/wine/%s", winePackage.getPackageFileName()));

        try {
            Files.createSymbolicLink(linkDestination.toPath(), localArchive.toPath());
        } catch (IOException e) {
            throw new EngineInstallException("Unable to install gecko. Failed to create symbolic link", e);
        }
    }

    private void installPackageInLocalCache(Observer progressControl) throws EngineInstallException {
        try {
            final URL packageUrl = winePackage.getPackageUrl();
            final HTTPDownloader httpDownloader = new HTTPDownloader(packageUrl);
            final File destinationFile = winePackage.getPackageDestination();
            final String packageChecksum = winePackage.getPackageChecksum();

            httpDownloader.addObserver(progressControl);
            httpDownloader.get(destinationFile);
            httpDownloader.deleteObservers();

            final ChecksumCalculator checksumCalculator = new ChecksumCalculator();
            checksumCalculator.addObserver(progressControl);
            final String clientSum = checksumCalculator.calculate(destinationFile, "md5");
            checksumCalculator.deleteObservers();

            if(!clientSum.equals(packageChecksum)) {
                //destinationFile.delete();
                throw new EngineInstallException(String.format("Error while downloading the file. Hash mismatch." +
                        System.lineSeparator() + System.lineSeparator() +
                        "Client hash:%s" + System.lineSeparator() +
                        "Server hash:%s", clientSum, packageChecksum));
            }

        } catch (MalformedURLException e) {
            throw new EngineInstallException("Package URL was malformed. Please report the problem", e);
        } catch (DownloadException e) {
            throw new EngineInstallException("Failed to download gecko", e);
        } catch (IOException e) {
            throw new EngineInstallException("Unable to calculate downloaded file checksum. Something went wrong", e);
        }

    }
}
