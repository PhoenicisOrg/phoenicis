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

package com.playonlinux.services.availableapplications;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.dto.ui.AppsItemDTO;
import com.playonlinux.dto.ui.AppsItemScriptDTO;
import com.playonlinux.dto.ui.CenterCategoryDTO;
import com.playonlinux.dto.ui.ProgressStateDTO;
import com.playonlinux.dto.web.ApplicationDTO;
import com.playonlinux.dto.web.CategoryDTO;
import com.playonlinux.dto.web.ScriptDTO;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.installer.InstallerSourceWebserviceImplementation;
import com.playonlinux.services.manager.AutoStartedService;
import com.playonlinux.services.manager.ServiceInitializationException;
import com.playonlinux.services.manager.ServiceManager;
import com.playonlinux.utils.comparator.AlphabeticalOrderComparator;
import com.playonlinux.utils.filter.Filter;
import com.playonlinux.utils.observer.AbstractObservableImplementation;
import com.playonlinux.utils.observer.Observable;
import com.playonlinux.webservice.DownloadEnvelope;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.playonlinux.dto.ui.AppsItemDTO.Builder;

@Scan
@AutoStartedService(name = "AvailableInstallersService")
public final class RemoteAvailableInstallersPlayOnLinuxImplementation extends AbstractObservableImplementation
        implements RemoteAvailableInstallers {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static ServiceManager playOnLinuxBackgroundServicesManager;

    private List<CategoryDTO> categoriesDTO = new ArrayList<>();
    private DownloadEnvelope<List<CategoryDTO>> downloadEnvelope;
    private InstallerSourceWebserviceImplementation installerSourceWebserviceImplementation;
    private URL webserviceUrl;

    private List<AppsItemDTO> cache = null;


    @Override
    public void update(Observable observable, DownloadEnvelope argument) {
        downloadEnvelope = (DownloadEnvelope<List<CategoryDTO>>) argument;

        try {
            if(downloadEnvelope.getEnvelopeContent() != null) {
                categoriesDTO = new ArrayList<>(
                        downloadEnvelope.getEnvelopeContent()
                );
            }
        } finally {
            cache = null; //invalidate cache
            this.notifyObservers();
        }

    }


    @Override
    public boolean isUpdating() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateDTO.State.PROGRESSING;
    }

    @Override
    public boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateDTO.State.FAILED;
    }

    @Override
    public List<AppsItemDTO> getFiltered(Filter<AppsItemDTO> filter) {
        updateCache();
        List<AppsItemDTO> filtered = new ArrayList<>();
        for(AppsItemDTO item : this.cache){
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
    public void refresh() throws ServiceInitializationException {
        if(installerSourceWebserviceImplementation != null) {
            installerSourceWebserviceImplementation.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(installerSourceWebserviceImplementation);
        }
        installerSourceWebserviceImplementation = new InstallerSourceWebserviceImplementation(webserviceUrl);
        installerSourceWebserviceImplementation.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(installerSourceWebserviceImplementation);
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

    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
    public void start() throws ServiceInitializationException {
        try {
            webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        } catch (MalformedURLException e) {
            throw new ServiceInitializationException(e);
        }
        this.refresh();
    }

}
