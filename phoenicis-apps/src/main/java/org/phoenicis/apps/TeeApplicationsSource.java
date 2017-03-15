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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.phoenicis.apps.dto.CategoryDTO;

public class TeeApplicationsSource extends MergeableApplicationsSource {
    private final ApplicationsSource leftApplicationsSource;
    private final ApplicationsSource rightApplicationsSource;

    /**
     * merges fetched applications from two sources
     * If an application is found in both sources, the leftApplicationsSource will be used.
     * @param leftApplicationsSource
     * @param rightApplicationsSource
     */
    protected TeeApplicationsSource(ApplicationsSource leftApplicationsSource,
                                    ApplicationsSource rightApplicationsSource) {
        this.leftApplicationsSource = leftApplicationsSource;
        this.rightApplicationsSource = rightApplicationsSource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
    	final Map<ApplicationsSource, List<CategoryDTO>> categoriesMap = Arrays.asList(leftApplicationsSource, rightApplicationsSource).stream()
    			.collect(
						Collectors.toMap(source -> source, ApplicationsSource::fetchInstallableApplications));
    	
    	return mergeApplicationsSources(categoriesMap, leftApplicationsSource, rightApplicationsSource);
    }
}
