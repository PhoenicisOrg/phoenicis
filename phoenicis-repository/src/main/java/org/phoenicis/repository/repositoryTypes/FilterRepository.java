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

package org.phoenicis.repository.repositoryTypes;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.tools.system.OperatingSystemFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Takes an Application Source and removes all unsupported scripts
 */
public class FilterRepository implements Repository {
    private final Repository repository;
    private final OperatingSystemFetcher operatingSystemFetcher;
    private final boolean enforceIncompatibleOperatingSystems;

    public FilterRepository(Repository repository, OperatingSystemFetcher operatingSystemFetcher,
            boolean enforceIncompatibleOperatingSystems) {
        this.repository = repository;
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.enforceIncompatibleOperatingSystems = enforceIncompatibleOperatingSystems;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final OperatingSystem currentOperatingSystem = operatingSystemFetcher.fetchCurrentOperationSystem();
        final List<CategoryDTO> categories = repository.fetchInstallableApplications();

        return categories.stream().map(category -> {
            final List<ApplicationDTO> applications = new ArrayList<>();
            for (ApplicationDTO application : category.getApplications()) {
                List<ScriptDTO> scripts = application.getScripts();
                if (!enforceIncompatibleOperatingSystems) {
                    scripts = application.getScripts().stream()
                            .filter(script -> script.getCompatibleOperatingSystems() == null
                                    || script.getCompatibleOperatingSystems().contains(currentOperatingSystem))
                            .collect(Collectors.toList());
                }
                if (!scripts.isEmpty()) {
                    applications.add(new ApplicationDTO.Builder(application).withScripts(scripts).build());
                }
            }

            return new CategoryDTO.Builder(category).withApplications(applications).build();
        }).filter(category -> !category.getApplications().isEmpty()).collect(Collectors.toList());
    }

    @Override
    public void onDelete() {
        this.repository.onDelete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilterRepository that = (FilterRepository) o;

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(enforceIncompatibleOperatingSystems, that.enforceIncompatibleOperatingSystems);
        builder.append(repository, that.repository);
        builder.append(operatingSystemFetcher, that.operatingSystemFetcher);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(enforceIncompatibleOperatingSystems);
        builder.append(repository);
        builder.append(enforceIncompatibleOperatingSystems);

        return builder.toHashCode();
    }
}
