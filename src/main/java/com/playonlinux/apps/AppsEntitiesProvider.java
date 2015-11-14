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

package com.playonlinux.apps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;
import com.playonlinux.apps.entities.AppEntity;
import com.playonlinux.apps.entities.AppsCategoryEntity;
import com.playonlinux.apps.entities.AppsWindowEntity;
import com.playonlinux.apps.entities.ScriptEntity;
import com.playonlinux.core.entities.ProgressStateEntity;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.ui.api.EntitiesProvider;

@Scan
public final class AppsEntitiesProvider extends ObservableDefaultImplementation<AppsWindowEntity>
        implements Observer<DefaultAppsManager, DefaultAppsManager>, EntitiesProvider<AppEntity, AppsWindowEntity> {

    @Inject
    static ServiceManager serviceManager;

    private final Collection<AppEntity> appsItemDTOs = new ArrayList<>();
    private final List<AppEntity> filteredAppsItemsDTOs = new ArrayList<>();
    private final List<AppsCategoryEntity> categoriesDTO = new ArrayList<>();

    private Filter<AppEntity> lastFilter;
    private DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope;

    @Override
    public void applyFilter(Filter<AppEntity> filter) {
        this.lastFilter = filter;

        if (filter == null) {
            filteredAppsItemsDTOs.clear();
        } else {
            filteredAppsItemsDTOs.clear();
            filteredAppsItemsDTOs.addAll(appsItemDTOs.stream().filter(filter::apply).collect(Collectors.toList()));
        }

        this.notifyObservers(
                new AppsWindowEntity.Builder().withAppsCategory(categoriesDTO).withAppsItem(filteredAppsItemsDTOs)
                        .withDownloadFailed(hasFailed()).withDownloading(isUpdating()).build());
    }

    private boolean isUpdating() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateEntity.State.PROGRESSING;
    }

    private boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateEntity.State.FAILED;
    }

    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
    public void init() throws ServiceInitializationException {
        final AppsManager appsManager = serviceManager.getService(AppsManager.class);
        appsManager.addObserver(this);
    }

    @Override
    public void update(DefaultAppsManager observable, DefaultAppsManager argument) {

        this.downloadEnvelope = argument.getDownloadEnvelope();
        this.categoriesDTO.clear();

        if (downloadEnvelope.getEnvelopeContent() != null) {
            for (CategoryDTO categoryDTO : downloadEnvelope.getEnvelopeContent()) {
                if (categoryDTO.getType() == CategoryDTO.CategoryType.INSTALLERS) {
                    categoriesDTO.add(new AppsCategoryEntity(categoryDTO.getName()));
                    for (ApplicationDTO applicationDTO : new ArrayList<>(categoryDTO.getApplications())) {
                        final List<ScriptEntity> scripts = new ArrayList<>();
                        for (ScriptDTO script : applicationDTO.getScripts()) {
                            scripts.add(new ScriptEntity.Builder().withName(script.getName()).withId(script.getId())
                                    .withUrl(script.getUrl()).build());
                        }

                        final AppEntity appsItemDTO = new AppEntity.Builder() //
                                .withName(applicationDTO.getName()) //
                                .withCategoryName(categoryDTO.getName()) //
                                .withDescription(applicationDTO.getDescription()) //
                                .withRequiresNoCd(false) // FIXME
                                .withTesting(false) //
                                .withCommercial(false) //
                                .withMiniaturesUrlsString(applicationDTO.getMiniaturesUrls()) //
                                .withScripts(scripts).build();

                        appsItemDTOs.add(appsItemDTO);
                    }
                }
            }
        }

        applyFilter(lastFilter);
    }
}
