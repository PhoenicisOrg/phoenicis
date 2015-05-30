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

package com.playonlinux.common.services;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.common.api.services.BackgroundServiceManager;
import com.playonlinux.common.comparator.AlphabeticalOrderComparator;
import com.playonlinux.common.dto.ApplicationDTO;
import com.playonlinux.common.dto.CategoryDTO;
import com.playonlinux.common.dto.DownloadEnvelopeDTO;
import com.playonlinux.common.dto.ProgressStateDTO;
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.webservice.InstallerSourceWebserviceImplementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Scan
public class RemoteAvailableInstallersPlayOnLinuxImplementation extends Observable
        implements com.playonlinux.common.api.services.RemoteAvailableInstallers, Observer {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static BackgroundServiceManager playOnLinuxBackgroundServicesManager;

    private List<CategoryDTO> categoriesDTO;
    private int numberOfCategories;
    private DownloadEnvelopeDTO<List<CategoryDTO>> downloadEnvelopeDto;
    private InstallerSourceWebserviceImplementation remoteAvailableInstallers;
    private final URL webserviceUrl;

    RemoteAvailableInstallersPlayOnLinuxImplementation() throws MalformedURLException {
        webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        this.refresh();
    }

    @Override
    public Iterator<CategoryDTO> iterator() {
        return new ArrayList<>(categoriesDTO).iterator();
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public void update(Observable o, Object arg) {
        assert(arg instanceof DownloadEnvelopeDTO);
        downloadEnvelopeDto = (DownloadEnvelopeDTO<List<CategoryDTO>>) arg;

        try {
            if(downloadEnvelopeDto.getEnvelopeContent() != null) {
                List<CategoryDTO> availableCategories = new ArrayList<>(
                        downloadEnvelopeDto.getEnvelopeContent()
                );

                numberOfCategories = availableCategories.size();
                categoriesDTO = availableCategories;
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
        return downloadEnvelopeDto.getDownloadState().getState() == ProgressStateDTO.State.PROGRESSING;
    }

    @Override
    public boolean hasFailed() {
        return downloadEnvelopeDto.getDownloadState().getState() == ProgressStateDTO.State.FAILED;
    }

    @Override
    public Iterable<ApplicationDTO> getAllScripts() {
        return getAllScripts(null);
    }

    @Override
    public Iterable<ApplicationDTO> getAllScripts(String filterText) {
        List<ApplicationDTO> applicationDTOs = new ArrayList<>();
        for(CategoryDTO categoryDTO: new ArrayList<>(categoriesDTO)) {
            for(ApplicationDTO applicationDTO: new ArrayList<>(categoryDTO.getApplications())) {
                if(filterText == null || applicationDTO.getName().contains(filterText)) {
                    applicationDTOs.add(applicationDTO);
                }
            }
        }

        Collections.sort(applicationDTOs, new AlphabeticalOrderComparator<ApplicationDTO>());

        return applicationDTOs::iterator;
    }

    @Override
    public Iterable<ApplicationDTO> getAllApplicationsInCategory(String categoryName) throws PlayOnLinuxException {
        for(CategoryDTO categoryDTO: categoriesDTO) {
            if(categoryName.equals(categoryDTO.getName())) {
                return getAllScriptsInCategory(categoryDTO);
            }
        }
        throw new PlayOnLinuxException(String.format("The category %s does not exist!", categoryName));
    }

    @Override
    public ApplicationDTO getApplicationByName(String scriptName) throws PlayOnLinuxException {
        for(ApplicationDTO applicationDTO: this.getAllScripts()) {
            if(scriptName.equals(applicationDTO.getName())) {
                return applicationDTO;
            }
        }

        throw new PlayOnLinuxException(String.format("The script %s does not exist!", scriptName));
    }

    @Override
    public void refresh() {
        if(remoteAvailableInstallers != null) {
            remoteAvailableInstallers.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(remoteAvailableInstallers);
        }
        remoteAvailableInstallers = new InstallerSourceWebserviceImplementation(webserviceUrl);
        remoteAvailableInstallers.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(remoteAvailableInstallers);
    }

    private Iterable<ApplicationDTO> getAllScriptsInCategory(CategoryDTO categoryDTO) {
        List<ApplicationDTO> applicationDTOs = new ArrayList<>();
        for(ApplicationDTO applicationDTO: new ArrayList<>(categoryDTO.getApplications())) {
            applicationDTOs.add(applicationDTO);
        }

        Collections.sort(applicationDTOs, new AlphabeticalOrderComparator<>());

        return applicationDTOs::iterator;
    }
}
