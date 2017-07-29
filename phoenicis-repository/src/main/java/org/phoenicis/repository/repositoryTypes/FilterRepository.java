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
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.repository.dto.*;
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
    public boolean isSafe() {
        return repository.isSafe();
    }

    @Override
    public RepositoryDTO fetchInstallableApplications() {
        final OperatingSystem currentOperatingSystem = operatingSystemFetcher.fetchCurrentOperationSystem();
        final RepositoryDTO repositoryDTO = repository.fetchInstallableApplications();
        final List<TypeDTO> types = repositoryDTO.getTypes();
        final List<TypeDTO> filteredTypes = types.stream().map(type -> {
            final List<CategoryDTO> categories = type.getCategories();
            final List<CategoryDTO> filteredCategories = categories.stream().map(category -> {
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
            return new TypeDTO.Builder(type).withCategories(filteredCategories).build();
        }).filter(type -> !type.getCategories().isEmpty()).collect(Collectors.toList());
        return new RepositoryDTO.Builder().withName(repositoryDTO.getName()).withTypes(filteredTypes)
                .withTranslations(repositoryDTO.getTranslations()).build();
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

        return new EqualsBuilder()
                .append(enforceIncompatibleOperatingSystems, that.enforceIncompatibleOperatingSystems)
                .append(repository, that.repository)
                .append(operatingSystemFetcher, that.operatingSystemFetcher)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(enforceIncompatibleOperatingSystems)
                .append(repository)
                .append(enforceIncompatibleOperatingSystems)
                .toHashCode();
    }
}
