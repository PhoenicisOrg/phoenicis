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

import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.engines.wine.entities.WineVersionDistributionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionsWindowEntity;
import com.playonlinux.engines.wine.dto.WineVersionDistributionWebDTO;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.AutoStartedService;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.core.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Scan
@AutoStartedService(type = WineVersionEntitiesProvider.class)
public final class WineVersionEntitiesProvider
        extends ObservableDefaultImplementation<WineVersionsWindowEntity>
        implements Observer<DefaultWineVersionsManager, DefaultWineVersionsManager>,
                   EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> {


    @Inject
    static ServiceManager serviceManager;

    private final List<WineVersionDistributionItemEntity> wineVersionDistributionItemEntities = new ArrayList<>();
    private final List<WineVersionDistributionItemEntity> filteredWineVersionDistributionItemEntities = new ArrayList<>();

    private Filter<WineVersionDistributionItemEntity> lastFilter;


    @Override
    public void update(DefaultWineVersionsManager observable, DefaultWineVersionsManager argument) {
        assert argument == observable;
        for (WineVersionDistributionWebDTO wineVersionDistributionDTO : new ArrayList<>(argument.getWineVersionDistributionDTOs())) {
            final List<WineVersionItemEntity> availablePackages = new ArrayList<>();
            final List<WineVersionItemEntity> installedPackages = new ArrayList<>();

            availablePackages.addAll(wineVersionDistributionDTO.getPackages().stream().map(wineVersionDTO -> new WineVersionItemEntity(wineVersionDTO.getVersion())).collect(Collectors.toList()));

            wineVersionDistributionItemEntities.add(new WineVersionDistributionItemEntity.Builder()
                            .withName(wineVersionDistributionDTO.getName())
                            .withAvailablePackages(availablePackages)
                            .withInstalledPackages(installedPackages)
                            .withDescription(wineVersionDistributionDTO.getDescription())
                            .build()
            );
        }


        applyFilter(lastFilter);
    }

    @Override
    public void applyFilter(Filter<WineVersionDistributionItemEntity> filter) {
        this.lastFilter = filter;

        filteredWineVersionDistributionItemEntities.clear();

        if(filter == null) {
            filteredWineVersionDistributionItemEntities.addAll(wineVersionDistributionItemEntities);
        } else {
            filteredWineVersionDistributionItemEntities.addAll(wineVersionDistributionItemEntities.stream().filter(filter::apply).collect(Collectors.toList()));
        }

        this.notifyObservers(new WineVersionsWindowEntity(filteredWineVersionDistributionItemEntities));
    }



    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
    public void start() throws ServiceInitializationException {
        final DefaultWineVersionsManager defaultWineVersionsManager = serviceManager.getService(DefaultWineVersionsManager.class);

        defaultWineVersionsManager.addObserver(this);
    }
}
