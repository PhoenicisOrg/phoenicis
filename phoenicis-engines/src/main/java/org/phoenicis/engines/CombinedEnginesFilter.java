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


import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;

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

    public void apply(List<EngineCategoryDTO> engines, String wineEnginesPath) {
        List<EngineCategoryDTO> filteredCategories = new ArrayList<>();
        for (EngineCategoryDTO engineCategoryDTO : engines) {
            List<EngineSubCategoryDTO> filteredDistributions = new ArrayList<>();
            for (EngineSubCategoryDTO engineSubCategoryDTO : engineCategoryDTO.getSubCategories()) {
                List<EngineVersionDTO> filteredPackages = new ArrayList<>();
                for (EngineVersionDTO engineVersionDTO : engineSubCategoryDTO.getPackages()) {
                    if (filters.contains(EnginesFilter.INSTALLED) && isInstalled(engineSubCategoryDTO, engineVersionDTO, wineEnginesPath)) {
                        filteredPackages.add(engineVersionDTO);
                    }
                    if (filters.contains(EnginesFilter.NOT_INSTALLED) && !isInstalled(engineSubCategoryDTO, engineVersionDTO, wineEnginesPath)) {
                        filteredPackages.add(engineVersionDTO);
                    }
                }
                engineSubCategoryDTO.getPackages().clear();
                engineSubCategoryDTO.getPackages().addAll(filteredPackages);
                if (engineSubCategoryDTO.getPackages().isEmpty()) {
                    filteredDistributions.add(engineSubCategoryDTO);
                }
            }
            engineCategoryDTO.getSubCategories().removeAll(filteredDistributions);
            if (engineCategoryDTO.getSubCategories().isEmpty()) {
                filteredCategories.add(engineCategoryDTO);
            }
        }
        engines.removeAll(filteredCategories);
    }

    private boolean isInstalled(EngineSubCategoryDTO engineCategoryDTO, EngineVersionDTO engineVersionDTO, String wineEnginesPath) {
        File f = new File(wineEnginesPath + "/" + engineCategoryDTO.getName() + "/" + engineVersionDTO.getVersion());
        return f.exists();
    }
}
