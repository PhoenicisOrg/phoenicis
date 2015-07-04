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

package com.playonlinux.domain;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.dto.ui.AppsItemScriptDTO;
import com.playonlinux.dto.web.*;
import com.playonlinux.services.BackgroundServiceManager;
import com.playonlinux.utils.filter.Filter;
import com.playonlinux.utils.comparator.AlphabeticalOrderComparator;
import com.playonlinux.dto.ui.CenterCategoryDTO;
import com.playonlinux.dto.ui.AppsItemDTO;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.installer.InstallerSourceWebserviceImplementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.playonlinux.dto.ui.AppsItemDTO.Builder;

@Scan
public final class RemoteAvailableInstallersPlayOnLinuxImplementation extends Observable
        implements RemoteAvailableInstallers, Observer {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static BackgroundServiceManager playOnLinuxBackgroundServicesManager;

    private List<CategoryDTO> categoriesDTO = new ArrayList<>();
    private DownloadEnvelopeDTO<List<CategoryDTO>> downloadEnvelopeDto;
    private InstallerSourceWebserviceImplementation remoteAvailableInstallers;
    private final URL webserviceUrl;

    private List<AppsItemDTO> cache = null;

    public RemoteAvailableInstallersPlayOnLinuxImplementation() throws MalformedURLException {
        webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        this.refresh();
    }

    @Override
    public Iterator<AppsItemDTO> iterator() {
        updateCache();
        return cache.iterator();
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public int size() {
        updateCache();
        return cache.size();
    }

    @Override
    public AppsItemDTO[] toArray() {
        return cache.toArray(new AppsItemDTO[cache.size()]);
    }

    @Override
    public void update(Observable o, Object arg) {
        assert(arg instanceof DownloadEnvelopeDTO);
        downloadEnvelopeDto = (DownloadEnvelopeDTO<List<CategoryDTO>>) arg;

        try {
            if(downloadEnvelopeDto.getEnvelopeContent() != null) {
                categoriesDTO = new ArrayList<>(
                        downloadEnvelopeDto.getEnvelopeContent()
                );
            }
        } finally {
            cache = null; //invalidate cache
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
    public ConList<AppsItemDTO> getFiltered(Filter<AppsItemDTO> filter) {
        List<AppsItemDTO> filtered = new ArrayList<>();
        for(AppsItemDTO item : this){
            if(filter.apply(item)){
                filtered.add(item);
            }
        }
        return filtered;
    }

    @Override
    public List<CenterCategoryDTO> getCategories() {
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


    private void updateCache(){
        if(cache == null){
            cache = new ArrayList<>();

            for(CategoryDTO categoryDTO: categoriesDTO) {
                if(categoryDTO.getType() == CategoryDTO.CategoryType.INSTALLERS) {
                    for (ApplicationDTO applicationDTO : new ArrayList<>(categoryDTO.getApplications())) {
                        List<AppsItemScriptDTO> scripts = new ArrayList<>();
                        for(ScriptDTO script: applicationDTO.getScripts()) {
                            scripts.add(
                                    new AppsItemScriptDTO.Builder()
                                        .withName(script.getName())
                                        .withId(script.getId())
                                        .build()
                            );
                        }

                        AppsItemDTO appsItemDTO = new Builder() //
                                .withName(applicationDTO.getName()) //
                                .withCategoryName(categoryDTO.getName()) //
                                .withDescription(applicationDTO.getDescription()) //
                                .withRequiresNoCd(false) // FIXME
                                .withTesting(false) //
                                .withCommercial(false) //
                                .withMiniaturesUrlsString(applicationDTO.getMiniaturesUrls()) //
                                .withScripts(scripts)
                                .build();
                        cache.add(appsItemDTO);
                    }
                }
            }
            Collections.sort(cache, new AlphabeticalOrderComparator<>());
        }
    }

}
