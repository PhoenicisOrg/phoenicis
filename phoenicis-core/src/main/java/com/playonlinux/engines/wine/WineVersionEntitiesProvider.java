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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.engines.wine.dto.WineVersionDistributionWebDTO;
import com.playonlinux.engines.wine.entities.WineVersionDistributionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionsWindowEntity;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.EntitiesProvider;

import lombok.Setter;

@Scan
public final class WineVersionEntitiesProvider
        implements EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> {

    @Inject
    static ServiceManager serviceManager;

    private final Collection<WineVersionDistributionItemEntity> wineVersionDistributionItemEntities = new ArrayList<>();
    private final List<WineVersionDistributionItemEntity> filteredWineVersionDistributionItemEntities = new ArrayList<>();

    private Predicate<WineVersionDistributionItemEntity> lastFilter;
    @Setter
    private Consumer<WineVersionsWindowEntity> onChange;

    public void update(WineVersionManager argument) {
        for (WineVersionDistributionWebDTO wineVersionDistributionDTO : new ArrayList<>(
                argument.getWineVersionDistributionDTOs())) {
            final List<WineVersionItemEntity> availablePackages = new ArrayList<>();
            final List<WineVersionItemEntity> installedPackages = new ArrayList<>();

            availablePackages.addAll(wineVersionDistributionDTO.getPackages().stream()
                    .map(wineVersionDTO -> new WineVersionItemEntity(wineVersionDTO.getVersion()))
                    .collect(Collectors.toList()));

            wineVersionDistributionItemEntities
                    .add(new WineVersionDistributionItemEntity.Builder().withName(wineVersionDistributionDTO.getName())
                            .withAvailablePackages(availablePackages).withInstalledPackages(installedPackages)
                            .withDescription(wineVersionDistributionDTO.getDescription()).build());
        }

        filter(lastFilter);
        if (onChange != null) {
            onChange.accept(new WineVersionsWindowEntity(filteredWineVersionDistributionItemEntities,
                    argument.isUpdating(), argument.hasFailed()));
        }
    }

    @Override
    public void filter(Predicate<WineVersionDistributionItemEntity> filter) {
        this.lastFilter = filter;

        filteredWineVersionDistributionItemEntities.clear();

        if (filter == null) {
            filteredWineVersionDistributionItemEntities.addAll(wineVersionDistributionItemEntities);
        } else {
            filteredWineVersionDistributionItemEntities
                    .addAll(wineVersionDistributionItemEntities.stream().filter(filter).collect(Collectors.toList()));
        }
    }

    @Override
    public void shutdown() {
        // Nothing to do
    }

    @Override
    public void init() {
        final DefaultWineVersionsManager defaultWineVersionsManager = serviceManager
                .getService(DefaultWineVersionsManager.class);

        defaultWineVersionsManager.setOnChange(this::update);
    }
}
