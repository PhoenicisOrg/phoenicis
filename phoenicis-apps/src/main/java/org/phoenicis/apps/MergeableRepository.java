/**
 * 
 */
package org.phoenicis.apps;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.apps.dto.ResourceDTO;
import org.phoenicis.apps.dto.ScriptDTO;

public abstract class MergeableRepository implements Repository {
	/**
	 * This method merges multiple application sources into a single list of
	 * category dtos. For this it receives a map, containing a binding between
	 * all application sources and their category dto lists, and an array
	 * containing all application sources that should be merged in the correct
	 * order. While merging the application source it prioritizes a later
	 * application source over an earlier one. This means, that if two
	 * application sources contain the same script, the script from the later
	 * application source is taken.
	 * 
	 * @param categoriesMap
	 *            A map containing a binding between the application sources and
	 *            their category dtos
	 * @param repositories
	 *            A list containing all application sources in the order in
	 *            which they should be merged
	 * @return A list containing category dtos of the merged application
	 *         sources. If no application sources were given, an empty list is
	 *         returned
	 */
	protected List<CategoryDTO> mergeRepositories(Map<Repository, List<CategoryDTO>> categoriesMap,
			Repository... repositories) {
		int numberOfRepositories = repositories.length;

		if (numberOfRepositories == 0) {
			return Collections.emptyList();
		}

		/*
		 * Take the first application source, from behind, as the default one
		 */
		final Map<String, CategoryDTO> mergedCategories = createSortedMap(
				categoriesMap.get(repositories[numberOfRepositories - 1]), CategoryDTO::getName);

		for (int otherRepositoryIndex = numberOfRepositories
				- 2; otherRepositoryIndex >= 0; otherRepositoryIndex--) {
			final List<CategoryDTO> otherCategories = categoriesMap
					.get(repositories[otherRepositoryIndex]);

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

	protected CategoryDTO mergeCategories(CategoryDTO leftCategory, CategoryDTO rightCategory) {
		final Map<String, ApplicationDTO> leftApplications = createSortedMap(leftCategory.getApplications(),
				ApplicationDTO::getName);
		final Map<String, ApplicationDTO> rightApplications = createSortedMap(rightCategory.getApplications(),
				ApplicationDTO::getName);

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
		return new CategoryDTO.Builder().withApplications(applications).withType(leftCategory.getType())
				.withIcon(leftCategory.getIcon()).withName(leftCategory.getName()).build();
	}

	protected ApplicationDTO mergeApplications(ApplicationDTO leftApplication, ApplicationDTO rightApplication) {
		final List<ScriptDTO> scripts = mergeListOfDtos(leftApplication.getScripts(), rightApplication.getScripts(),
				ScriptDTO::getScriptName, ScriptDTO.nameComparator());
		final List<ResourceDTO> resources = mergeListOfDtos(leftApplication.getResources(),
				rightApplication.getResources(), ResourceDTO::getName, ResourceDTO.nameComparator());

		final Set<ByteBuffer> mergeMiniaturesSet = new HashSet<>();
		leftApplication.getMiniatures().forEach(miniature -> mergeMiniaturesSet.add(ByteBuffer.wrap(miniature)));
		rightApplication.getMiniatures().forEach(miniature -> mergeMiniaturesSet.add(ByteBuffer.wrap(miniature)));

		final List<byte[]> mergeMiniatures = new ArrayList<>();
		mergeMiniaturesSet.forEach(miniature -> mergeMiniatures.add(miniature.array()));

		return new ApplicationDTO.Builder().withName(leftApplication.getName()).withResources(resources)
				.withScripts(scripts).withDescription(leftApplication.getDescription())
				.withIcon(leftApplication.getIcon()).withMiniatures(mergeMiniatures).build();
	}

	protected <T> List<T> mergeListOfDtos(List<T> leftList, List<T> rightList, Function<T, String> nameSupplier,
			Comparator<T> sorter) {
		final Map<String, T> left = createSortedMap(leftList, nameSupplier);
		final Map<String, T> right = createSortedMap(rightList, nameSupplier);

		final SortedMap<String, T> merged = new TreeMap<>(left);

		for (String name : right.keySet()) {
			final T dto = right.get(name);

			if (!merged.containsKey(name)) {
				merged.put(name, dto);
			}
		}

		final List<T> result = new ArrayList<>(merged.values());
		result.sort(sorter);
		return result;
	}

	protected <T> Map<String, T> createSortedMap(List<T> dtos, Function<T, String> nameProvider) {
		final SortedMap<String, T> map = new TreeMap<>();
		dtos.forEach(dto -> map.put(nameProvider.apply(dto), dto));
		return map;
	}
}
