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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.phoenicis.apps.dto.CategoryDTO;

public class TeeApplicationsSource implements MergeableApplicationsSource {
    private final ApplicationsSource leftApplicationSource;
    private final ApplicationsSource rightApplicationSource;

    /**
     * merges fetched applications from two sources
     * If an application is found in both sources, the leftApplicationSource will be used.
     * @param leftApplicationSource
     * @param rightApplicationSource
     */
    protected TeeApplicationsSource(ApplicationsSource leftApplicationSource,
                                    ApplicationsSource rightApplicationSource) {
        this.leftApplicationSource = leftApplicationSource;
        this.rightApplicationSource = rightApplicationSource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final Map<String, CategoryDTO> leftCategories = createSortedMap(leftApplicationSource.fetchInstallableApplications(), CategoryDTO::getName);
        final Map<String, CategoryDTO> rightCategories = createSortedMap(rightApplicationSource.fetchInstallableApplications(), CategoryDTO::getName);

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

    
}
