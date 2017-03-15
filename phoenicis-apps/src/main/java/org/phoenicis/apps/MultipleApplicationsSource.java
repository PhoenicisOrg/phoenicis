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

class MultipleApplicationsSource extends MergeableApplicationsSource {
	private final ApplicationsSource[] applicationsSources;

	MultipleApplicationsSource(ApplicationsSource... applicationsSources) {
		this.applicationsSources = applicationsSources;
	}

	MultipleApplicationsSource(List<ApplicationsSource> applicationsSources) {
		this(applicationsSources.toArray(new ApplicationsSource[0]));
	}

	@Override
	public List<CategoryDTO> fetchInstallableApplications() {
		/*
		 * This step is needed because we need a mapping between the CategoryDTO
		 * list and its application source, to preserve the order in the
		 * reduction step
		 */
		final Map<ApplicationsSource, List<CategoryDTO>> categoriesMap = Arrays.stream(this.applicationsSources)
				.parallel().collect(
						Collectors.toConcurrentMap(source -> source, ApplicationsSource::fetchInstallableApplications));

		return mergeApplicationsSources(categoriesMap, applicationsSources);
	}
}
