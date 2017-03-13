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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.phoenicis.apps.dto.CategoryDTO;

class MultipleApplicationsSource implements MergeableApplicationsSource {
	private final List<ApplicationsSource> applicationsSources;

	MultipleApplicationsSource(ApplicationsSource... applicationsSources) {
		this(Arrays.asList(applicationsSources));
	}

	MultipleApplicationsSource(List<ApplicationsSource> applicationsSources) {
		this.applicationsSources = applicationsSources;
	}

	@Override
	public List<CategoryDTO> fetchInstallableApplications() {
		/*
		 * This step is needed because we need a mapping between the CategoryDTO
		 * list and its application source, to preserve the order in the
		 * reduction step
		 */
		final Map<ApplicationsSource, List<CategoryDTO>> categoriesMap = this.applicationsSources.parallelStream()
				.collect(
						Collectors.toConcurrentMap(source -> source, ApplicationsSource::fetchInstallableApplications));

		if (applicationsSources.isEmpty())
			return Collections.emptyList();

		final List<ApplicationsSource> tmpApplicationsSources = new ArrayList<>(applicationsSources);

		/*
		 * Reverse the list, because we need to prioritize a later application
		 * source higher than an earlier one
		 */
		Collections.reverse(tmpApplicationsSources);

		/*
		 * Take the first application source, from behind, as the default one
		 * and remove it from the list of remaining application sources
		 */
		final Map<String, CategoryDTO> mergedCategories = createSortedMap(
				categoriesMap.get(tmpApplicationsSources.remove(0)), CategoryDTO::getName);

		for (ApplicationsSource otherApplicationsSource : tmpApplicationsSources) {
			final List<CategoryDTO> otherCategories = categoriesMap.get(otherApplicationsSource);

			final Map<String, CategoryDTO> otherCategoriesMap = createSortedMap(otherCategories, CategoryDTO::getName);

			for (String categoryName : otherCategoriesMap.keySet()) {
				final CategoryDTO category = otherCategoriesMap.get(categoryName);

				if (mergedCategories.containsKey(categoryName)) {
					mergedCategories.put(categoryName, mergeCategories(mergedCategories.get(categoryName), category));
				} else {
					mergedCategories.put(categoryName, category);
				}
			}
		}

		return new ArrayList<>(mergedCategories.values());
	}
}
