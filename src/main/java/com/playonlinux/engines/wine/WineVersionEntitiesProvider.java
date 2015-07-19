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

import com.playonlinux.dto.ui.engines.WineVersionDistributionItemDTO;
import com.playonlinux.dto.ui.engines.WineVersionItemDTO;
import com.playonlinux.dto.ui.engines.WineVersionsWindowDTO;
import com.playonlinux.dto.web.WineVersionDTO;
import com.playonlinux.dto.web.WineVersionDistributionDTO;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.AutoStartedService;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.core.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Scan
@AutoStartedService(type = WineVersionEntitiesProvider.class)
public final class WineVersionEntitiesProvider
        extends AbstractObservableImplementation<WineVersionsWindowDTO>
        implements Observer<WineVersionsManager, WineVersionsManager>,
                   EntitiesProvider<WineVersionDistributionItemDTO, WineVersionsWindowDTO> {


    @Inject
    static ServiceManager serviceManager;

    private final List<WineVersionDistributionItemDTO> wineVersionDistributionItemDTOs = new ArrayList<>();
    private final List<WineVersionDistributionItemDTO> filteredWineVersionDistributionItemDTOs = new ArrayList<>();

    private Filter<WineVersionDistributionItemDTO> lastFilter;


    @Override
    public void update(WineVersionsManager observable, WineVersionsManager argument) {
        assert argument == observable;
        for (WineVersionDistributionDTO wineVersionDistributionDTO : new ArrayList<>(argument.getWineVersionDistributionDTOs())) {
            final List<WineVersionItemDTO> availablePackages = new ArrayList<>();
            final List<WineVersionItemDTO> installedPackages = new ArrayList<>();

            availablePackages.addAll(wineVersionDistributionDTO.getPackages().stream().map(wineVersionDTO -> new WineVersionItemDTO(wineVersionDTO.getVersion())).collect(Collectors.toList()));

            wineVersionDistributionItemDTOs.add(new WineVersionDistributionItemDTO.Builder()
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
    public void applyFilter(Filter<WineVersionDistributionItemDTO> filter) {
        this.lastFilter = filter;

        filteredWineVersionDistributionItemDTOs.clear();

        if(filter == null) {
            filteredWineVersionDistributionItemDTOs.addAll(wineVersionDistributionItemDTOs);
        } else {
            filteredWineVersionDistributionItemDTOs.addAll(wineVersionDistributionItemDTOs.stream().filter(filter::apply).collect(Collectors.toList()));
        }

        this.notifyObservers(new WineVersionsWindowDTO(filteredWineVersionDistributionItemDTOs));
    }



    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
    public void start() throws ServiceInitializationException {
        final WineVersionsManager wineVersionsManager = serviceManager.getBackgroundService(WineVersionsManager.class);

        wineVersionsManager.addObserver(this);
    }
}
