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

import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.apps.dto.ResourceDTO;
import org.phoenicis.apps.dto.ScriptDTO;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;

public class TeeRepositorySource implements RepositorySource {
    private final RepositorySource leftRepositorySource;
    private final RepositorySource rightRepositorySource;

    /**
     * merges fetched applications from two sources
     * If an application is found in both sources, the leftRepositorySource will be used.
     * @param leftRepositorySource
     * @param rightRepositorySource
     */
    protected TeeRepositorySource(RepositorySource leftRepositorySource,
                                    RepositorySource rightRepositorySource) {
        this.leftRepositorySource = leftRepositorySource;
        this.rightRepositorySource = rightRepositorySource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final Map<String, CategoryDTO> leftCategories = createSortedMap(leftRepositorySource.fetchInstallableApplications(), CategoryDTO::getName);
        final Map<String, CategoryDTO> rightCategories = createSortedMap(rightRepositorySource.fetchInstallableApplications(), CategoryDTO::getName);

        final SortedMap<String, CategoryDTO> mergedCategories = new TreeMap<>(rightCategories);

        for (String categoryName : leftCategories.keySet()) {
            final CategoryDTO category = leftCategories.get(categoryName);

            if (mergedCategories.containsKey(categoryName)) {
                mergedCategories.put(categoryName, mergeCategories(mergedCategories.get(categoryName), category));
            } else {
                mergedCategories.put(categoryName, category);
            }
        }

        return new ArrayList<>(mergedCategories.values());
    }

    private CategoryDTO mergeCategories(CategoryDTO leftCategory, CategoryDTO rightCategory) {
        final Map<String, ApplicationDTO> leftApplications = createSortedMap(leftCategory.getApplications(), ApplicationDTO::getName);
        final Map<String, ApplicationDTO> rightApplications = createSortedMap(rightCategory.getApplications(), ApplicationDTO::getName);

        final SortedMap<String, ApplicationDTO> mergedApps = new TreeMap<>(rightApplications);

        for (String applicationName : leftApplications.keySet()) {
            final ApplicationDTO application = leftApplications.get(applicationName);

            if (mergedApps.containsKey(applicationName)) {
                mergedApps.put(applicationName, mergeApplications(mergedApps.get(applicationName), application));
            } else {
                mergedApps.put(applicationName, application);
            }
        }

        final List<ApplicationDTO> applications = new ArrayList<>(mergedApps.values());
        applications.sort(ApplicationDTO.nameComparator());
        return new CategoryDTO.Builder()
                .withApplications(applications)
                .withType(leftCategory.getType())
                .withIcon(leftCategory.getIcon())
                .withName(leftCategory.getName())
                .build();
    }

    private ApplicationDTO mergeApplications(ApplicationDTO leftApplication,
                                             ApplicationDTO rightApplication) {
        final List<ScriptDTO> scripts = mergeListOfDtos(leftApplication.getScripts(), rightApplication.getScripts(), ScriptDTO::getName, ScriptDTO.nameComparator());
        final List<ResourceDTO> resources = mergeListOfDtos(leftApplication.getResources(), rightApplication.getResources(), ResourceDTO::getName, ResourceDTO.nameComparator());

        final Set<ByteBuffer> mergeMiniaturesSet = new HashSet<>();
        leftApplication.getMiniatures().forEach(miniature -> mergeMiniaturesSet.add(ByteBuffer.wrap(miniature)));
        rightApplication.getMiniatures().forEach(miniature -> mergeMiniaturesSet.add(ByteBuffer.wrap(miniature)));

        final List<byte[]> mergeMiniatures = new ArrayList();
        mergeMiniaturesSet.forEach(miniature -> mergeMiniatures.add(miniature.array()));

        return new ApplicationDTO.Builder()
                .withName(leftApplication.getName())
                .withResources(resources)
                .withScripts(scripts)
                .withDescription(leftApplication.getDescription())
                .withIcon(leftApplication.getIcon())
                .withMiniatures(mergeMiniatures)
                .build();
    }



    private <T> List<T> mergeListOfDtos(List<T> leftList, List<T> rightList, Function<T, String> nameSupplier, Comparator<T> sorter) {
        final Map<String, T> left = createSortedMap(leftList, nameSupplier);
        final Map<String, T> right = createSortedMap(rightList, nameSupplier);

        final SortedMap<String, T> merged = new TreeMap<>(left);

        for (String name: right.keySet()) {
            final T dto = right.get(name);

            if (!merged.containsKey(name)) {
                merged.put(name, dto);
            }
        }

        final List<T> result = new ArrayList<>(merged.values());
        result.sort(sorter);
        return result;
    }

    private <T> Map<String, T> createSortedMap(List<T> dtos, Function<T, String> nameProvider) {
        final SortedMap<String, T> map = new TreeMap<>();
        dtos.forEach(dto -> map.put(nameProvider.apply(dto), dto));
        return map;
    }
}
