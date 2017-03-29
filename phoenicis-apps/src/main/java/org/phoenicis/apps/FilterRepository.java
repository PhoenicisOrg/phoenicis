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

package org.phoenicis.apps;

import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.apps.dto.ScriptDTO;
import org.phoenicis.tools.system.OperatingSystemFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Takes an Application Source and removes all unsupported scripts
 */
class FilterRepository implements Repository {
    private final Repository repository;
    private final OperatingSystemFetcher operatingSystemFetcher;
    private final boolean enforceIncompatibleOperatingSystems;
    private CombinedAppsFilter filter;

    FilterRepository(Repository repository,
                             OperatingSystemFetcher operatingSystemFetcher,
                             boolean enforceIncompatibleOperatingSystems) {
        this.repository = repository;
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.enforceIncompatibleOperatingSystems = enforceIncompatibleOperatingSystems;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final OperatingSystem currentOperatingSystem = operatingSystemFetcher.fetchCurrentOperationSystem();
        final List<CategoryDTO> categories = repository.fetchInstallableApplications();
        final List<CategoryDTO> filteredCategories = new ArrayList<>();
        for (CategoryDTO category : categories) {
            final List<ApplicationDTO> applications = new ArrayList<>();
            for (ApplicationDTO application : category.getApplications()) {
                final List<ScriptDTO> scripts;
                if(enforceIncompatibleOperatingSystems) {
                    scripts = application.getScripts();
                } else {
                    scripts = application.getScripts().stream().filter(script -> script.getCompatibleOperatingSystems() == null || script.getCompatibleOperatingSystems().contains(currentOperatingSystem)).collect(Collectors.toList());
                }
                if (!scripts.isEmpty()) {
                    if (filter == null || category.getType() != CategoryDTO.CategoryType.INSTALLERS || filter.applies(application)) {
                        applications.add(
                                new ApplicationDTO.Builder(application)
                                        .withScripts(scripts)
                                        .build()
                        );
                    }
                }
            }
            if (!applications.isEmpty()) {
                filteredCategories.add(
                        new CategoryDTO.Builder(category)
                                .withApplications(applications)
                                .build()
                );
            }
        }

        return filteredCategories;
    }

    @Override
    public void onDelete() {
        this.repository.onDelete();
    }

    @Override
    public void setFilter(CombinedAppsFilter filter) {
        this.filter = filter;
    }
}
