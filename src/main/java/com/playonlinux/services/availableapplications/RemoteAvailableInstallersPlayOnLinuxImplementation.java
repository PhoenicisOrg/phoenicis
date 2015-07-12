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
import com.playonlinux.dto.ui.ProgressStateDTO;
import com.playonlinux.dto.ui.apps.AppsCategoryDTO;
import com.playonlinux.dto.ui.apps.AppsItemDTO;
import com.playonlinux.dto.ui.apps.AppsItemScriptDTO;
import com.playonlinux.dto.ui.apps.AppsWindowDTO;
import com.playonlinux.dto.web.ApplicationDTO;
import com.playonlinux.dto.web.CategoryDTO;
import com.playonlinux.dto.web.ScriptDTO;
import com.playonlinux.filter.Filter;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.installer.InstallerSource;
import com.playonlinux.installer.InstallerSourceWebserviceImplementation;
import com.playonlinux.services.manager.AutoStartedService;
import com.playonlinux.services.manager.ServiceInitializationException;
import com.playonlinux.services.manager.ServiceManager;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.utils.observer.AbstractObservableImplementation;
import com.playonlinux.utils.observer.Observer;
import com.playonlinux.webservice.DownloadEnvelope;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.playonlinux.dto.ui.apps.AppsItemDTO.Builder;

@Scan
@AutoStartedService(name = "AvailableInstallersService")
public final class RemoteAvailableInstallersPlayOnLinuxImplementation
        extends AbstractObservableImplementation<AppsWindowDTO>
        implements Observer<InstallerSource, DownloadEnvelope<Collection<CategoryDTO>>>,
                   EntitiesProvider<AppsItemDTO, AppsWindowDTO> {

    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static ServiceManager playOnLinuxBackgroundServicesManager;

    private List<AppsItemDTO> appsItemDTOs = new ArrayList<>();
    private List<AppsItemDTO> filteredAppsItemsDTOs = new ArrayList<>();
    private List<AppsCategoryDTO> categoriesDTO = new ArrayList<>();

    private InstallerSourceWebserviceImplementation installerSourceWebserviceImplementation;
    private URL webserviceUrl;
    private Filter<AppsItemDTO> lastFilter;
    private DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope;


    @Override
    public void update(InstallerSource installerSource, DownloadEnvelope<Collection<CategoryDTO>> downloadEnvelope) {
        this.downloadEnvelope = downloadEnvelope;
        this.categoriesDTO.clear();

        if(downloadEnvelope.getEnvelopeContent() != null) {
            for (CategoryDTO categoryDTO : downloadEnvelope.getEnvelopeContent()) {
                if (categoryDTO.getType() == CategoryDTO.CategoryType.INSTALLERS) {
                    categoriesDTO.add(new AppsCategoryDTO(categoryDTO.getName()));
                    for (ApplicationDTO applicationDTO : new ArrayList<>(categoryDTO.getApplications())) {
                        List<AppsItemScriptDTO> scripts = new ArrayList<>();
                        for (ScriptDTO script : applicationDTO.getScripts()) {
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

                        appsItemDTOs.add(appsItemDTO);
                    }
                }
            }
        }
        applyFilter(lastFilter);
    }

    @Override
    public void applyFilter(Filter<AppsItemDTO> filter) {
        this.lastFilter = filter;

        if(filter == null) {
            filteredAppsItemsDTOs.clear();
        } else {
            filteredAppsItemsDTOs.clear();
            for (AppsItemDTO appsItemDTO : appsItemDTOs) {
                if (filter.apply(appsItemDTO)) {
                    filteredAppsItemsDTOs.add(appsItemDTO);
                }
            }
        }


        this.notifyObservers(new AppsWindowDTO.Builder()
                .withAppsCategory(categoriesDTO)
                .withAppsItem(filteredAppsItemsDTOs)
                .withDownloadFailed(hasFailed())
                .withDownloading(isUpdating())
                .build());
    }


    private boolean isUpdating() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateDTO.State.PROGRESSING;
    }

    private boolean hasFailed() {
        return downloadEnvelope.getDownloadState().getState() == ProgressStateDTO.State.FAILED;
    }


    public void refresh() throws ServiceInitializationException {
        if(installerSourceWebserviceImplementation != null) {
            installerSourceWebserviceImplementation.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(installerSourceWebserviceImplementation);
        }
        installerSourceWebserviceImplementation = new InstallerSourceWebserviceImplementation(webserviceUrl);
        installerSourceWebserviceImplementation.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(installerSourceWebserviceImplementation);
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
