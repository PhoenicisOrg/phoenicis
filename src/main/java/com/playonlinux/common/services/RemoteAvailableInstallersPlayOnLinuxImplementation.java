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
import com.playonlinux.common.dto.ui.CenterCategoryDTO;
import com.playonlinux.common.dto.ui.CenterItemDTO;
import com.playonlinux.common.dto.web.ApplicationDTO;
import com.playonlinux.common.dto.web.CategoryDTO;
import com.playonlinux.common.dto.web.DownloadEnvelopeDTO;
import com.playonlinux.common.dto.web.ProgressStateDTO;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.webservice.InstallerSourceWebserviceImplementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.playonlinux.common.dto.ui.CenterItemDTO.Builder;

@Scan
public class RemoteAvailableInstallersPlayOnLinuxImplementation extends Observable
        implements com.playonlinux.common.api.services.RemoteAvailableInstallers, Observer {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static BackgroundServiceManager playOnLinuxBackgroundServicesManager;

    private List<CategoryDTO> categoriesDTO = new ArrayList<>();
    private DownloadEnvelopeDTO<List<CategoryDTO>> downloadEnvelopeDto;
    private InstallerSourceWebserviceImplementation remoteAvailableInstallers;
    private final URL webserviceUrl;

    RemoteAvailableInstallersPlayOnLinuxImplementation() throws MalformedURLException {
        webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        this.refresh();
    }

    @Override
    public Iterator<CenterItemDTO> iterator() {
        return getAllCenterItems().iterator();
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

                categoriesDTO = availableCategories;
            }
        } finally {
            this.setChanged();
            this.notifyObservers();
        }

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
    public List<CenterItemDTO> getAllCenterItems() {
        List<CenterItemDTO> centerItemDTOs = new ArrayList<>();

        for(CategoryDTO categoryDTO: categoriesDTO) {

            for(ApplicationDTO applicationDTO: new ArrayList<>(categoryDTO.getApplications())) {
                CenterItemDTO centerItemDTO = new Builder() //
                        .withName(applicationDTO.getName()) //
                        .withCategoryName(categoryDTO.getName()) //
                        .withDescription(applicationDTO.getDescription()) //
                        .withRequiresNoCd(false) // FIXME
                        .withTesting(false) //
                        .withCommercial(false) //
                        .build();
                centerItemDTOs.add(centerItemDTO);
            }
        }
        Collections.sort(centerItemDTOs, new AlphabeticalOrderComparator<>());

        return centerItemDTOs;
    }

    @Override
    public List<CenterCategoryDTO> getAllCategories() {
        List <CenterCategoryDTO> categoryDTOs = new ArrayList<>();
        for(CategoryDTO categoryDTO: categoriesDTO) {
            if(categoryDTO.getType() == CategoryDTO.CategoryType.INSTALLERS) {
                categoryDTOs.add(new CenterCategoryDTO(categoryDTO.getName()));
            }
        }
        return categoryDTOs;
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


}
