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

package com.playonlinux.app;

import com.playonlinux.common.dtos.AvailableCategoriesDTO;
import com.playonlinux.common.dtos.CategoryDTO;
import com.playonlinux.common.dtos.DownloadEnvelopeDTO;
import com.playonlinux.common.dtos.DownloadStateDTO;
import com.playonlinux.injection.Scan;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.webservice.RemoteWebService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Scan
public class RemoteAvailableInstallersPlayOnLinuxImplementation extends Observable
        implements RemoteAvailableInstallers, Observer {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private Iterator<CategoryDTO> categoryDTO;
    private int numberOfCategories;
    private DownloadEnvelopeDTO<AvailableCategoriesDTO> downloadEnvelopeDto;

    RemoteAvailableInstallersPlayOnLinuxImplementation() throws MalformedURLException {
        URL webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        RemoteWebService remoteWebService = new RemoteWebService(webserviceUrl);
        remoteWebService.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(remoteWebService);
    }

    @Override
    public Iterator<CategoryDTO> iterator() {
        return categoryDTO;
    }

    @Override
    public void update(Observable o, Object arg) {
        downloadEnvelopeDto = (DownloadEnvelopeDTO<AvailableCategoriesDTO>) arg;

        try {
            if(downloadEnvelopeDto.getEnvelopeContent() != null) {
                ArrayList<CategoryDTO> availableCategories = new ArrayList<>(
                        downloadEnvelopeDto.getEnvelopeContent().getCategories()
                );
                numberOfCategories = availableCategories.size();
                categoryDTO = availableCategories.iterator();
            }
        } finally {
            this.setChanged();
            this.notifyObservers();
        }

    }

    @Override
    public int getNumberOfCategories() {
        return numberOfCategories;
    }

    @Override
    public boolean isUpdating() {
        return downloadEnvelopeDto.getDownloadState().getState() == DownloadStateDTO.State.DOWNLOADING;
    }

    @Override
    public boolean hasFailed() {
        return downloadEnvelopeDto.getDownloadState().getState() == DownloadStateDTO.State.FAILED;
    }
}
