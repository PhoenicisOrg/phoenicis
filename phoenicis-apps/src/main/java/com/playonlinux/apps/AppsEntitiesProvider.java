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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.entities.AppEntity;
import com.playonlinux.apps.entities.AppsCategoryEntity;
import com.playonlinux.apps.entities.AppsWindowEntity;
import com.playonlinux.apps.entities.ScriptEntity;
import com.playonlinux.core.entities.ProgressState;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.webservice.DownloadEnvelope;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.EntitiesProvider;

import lombok.Setter;

@Scan
public final class AppsEntitiesProvider implements EntitiesProvider<AppEntity, AppsWindowEntity> {

    @Inject
    static ServiceManager serviceManager;

    private final Collection<AppEntity> appsItemDTOs = new ArrayList<>();
    private final List<AppEntity> filteredAppsItemsDTOs = new ArrayList<>();
    private final List<AppsCategoryEntity> categoriesDTO = new ArrayList<>();

    private Predicate<AppEntity> lastFilter;
    private DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope;
    
    @Setter
    private Consumer<AppsWindowEntity> onChange;

    @Override
    public void filter(Predicate<AppEntity> filter) {
        this.lastFilter = filter;

        filteredAppsItemsDTOs.clear();
        if (filter != null) {
            filteredAppsItemsDTOs.addAll(appsItemDTOs.stream().filter(filter).collect(Collectors.toList()));
        }

        if (onChange != null) {
            onChange.accept(
                    new AppsWindowEntity.Builder().withAppsCategory(categoriesDTO).withAppsItem(filteredAppsItemsDTOs)
                            .withDownloadFailed(hasFailed()).withDownloading(isUpdating()).build());
        }
    }

    private boolean isUpdating() {
        return downloadEnvelope.getDownloadState().getState() == ProgressState.PROGRESSING;
    }

    private boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressState.FAILED;
    }

    @Override
    public void shutdown() {
        //Nothing to do
    }

    @Override
    public void init() {
        final AppsManager appsManager = serviceManager.getService(AppsManager.class);
        appsManager.setOnChange(() -> this.update(appsManager));
    }

    public void update(AppsManager appsManager) {

        this.downloadEnvelope = appsManager.getDownloadEnvelope();
        this.categoriesDTO.clear();

        if (downloadEnvelope.getEnvelopeContent() != null) {
            downloadEnvelope.getEnvelopeContent().stream()
                    .filter(categoryDTO -> categoryDTO.getType() == CategoryDTO.CategoryType.INSTALLERS)
                    .forEach(categoryDTO -> {
                        categoriesDTO.add(new AppsCategoryEntity(categoryDTO.getName()));
                        for (ApplicationDTO applicationDTO : new ArrayList<>(categoryDTO.getApplications())) {
                            final List<ScriptEntity> scripts = applicationDTO.getScripts().stream()
                                    .map(script -> new ScriptEntity.Builder().withName(script.getName())
                                            .withId(script.getId()).withUrl(script.getUrl()).build())
                                    .collect(Collectors.toList());

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
                    });
        }

        filter(lastFilter);
    }
}
