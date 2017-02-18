/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.engines;


import org.phoenicis.engines.dto.WineVersionDTO;
import org.phoenicis.engines.dto.WineVersionDistributionDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinedEnginesFilter {
    private final Set<EnginesFilter> filters = new HashSet<>();

    public CombinedEnginesFilter() {

    }

    public void remove(EnginesFilter filter) {
        filters.remove(filter);
    }

    public void add(EnginesFilter filter) {
        filters.add(filter);
    }

    public void apply(List<WineVersionDistributionDTO> engines, String wineEnginesPath) {
        List<WineVersionDistributionDTO> filteredDistributions = new ArrayList<>();
        for (WineVersionDistributionDTO wineVersionDistributionDTO : engines) {
            List<WineVersionDTO> filteredPackages = new ArrayList<>();
            for (WineVersionDTO wineVersionDTO : wineVersionDistributionDTO.getPackages()) {
                if (filters.contains(EnginesFilter.INSTALLED) && isInstalled(wineVersionDistributionDTO, wineVersionDTO, wineEnginesPath)) {
                    filteredPackages.add(wineVersionDTO);
                }
                if (filters.contains(EnginesFilter.NOT_INSTALLED) && !isInstalled(wineVersionDistributionDTO, wineVersionDTO, wineEnginesPath)) {
                    filteredPackages.add(wineVersionDTO);
                }
            }
            wineVersionDistributionDTO.getPackages().clear();
            wineVersionDistributionDTO.getPackages().addAll(filteredPackages);
            if (wineVersionDistributionDTO.getPackages().isEmpty()) {
                filteredDistributions.add(wineVersionDistributionDTO);
            }
        }
        engines.removeAll(filteredDistributions);
    }

    private boolean isInstalled(WineVersionDistributionDTO wineVersionDistributionDTO, WineVersionDTO wineVersionDTO, String wineEnginesPath) {
        File f = new File(wineEnginesPath + "/" + wineVersionDistributionDTO.getName() + "/" + wineVersionDTO.getVersion());
        return f.exists();
    }
}
