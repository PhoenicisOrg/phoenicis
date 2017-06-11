/**
 *
 */
package org.phoenicis.repository.repositoryTypes;

import org.apache.commons.codec.digest.DigestUtils;
import org.phoenicis.configuration.localisation.Localisation;
import org.phoenicis.configuration.localisation.PropertiesResourceBundle;
import org.phoenicis.repository.dto.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Function;

public abstract class MergeableRepository implements Repository {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MergeableRepository.class);

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
     * @param repositoriesMap A map containing a binding between the application sources and
     *                      their repository DTO
     * @param repositories  A list containing all application sources in the order in
     *                      which they should be merged
     * @return A list containing category dtos of the merged application
     * sources. If no application sources were given, an empty list is
     * returned
     */
    protected RepositoryDTO mergeRepositories(Map<Repository, RepositoryDTO> repositoriesMap,
            List<Repository> repositories) {
        int numberOfRepositories = repositories.size();

        if (numberOfRepositories == 0) {
            return null;
        }

        RepositoryDTO.Builder repositoryDTOBuilder = new RepositoryDTO.Builder().withName("merged repository");

        Properties translationProperties = new Properties();
        Map<Repository, List<CategoryDTO>> categoriesMap = new HashMap<>();
        for (Repository repository : repositoriesMap.keySet()) {
            RepositoryDTO repositoryDTO = repositoriesMap.get(repository);
            translationProperties.putAll(repositoryDTO.getTranslations().getProperties());
            categoriesMap.put(repository, repositoryDTO.getCategories());
        }
        repositoryDTOBuilder.withTranslations(new TranslationDTO.Builder()
                .withLanguage(Locale.getDefault().getLanguage()).withProperties(translationProperties).build());
        Localisation.setAdditionalTranslations(new PropertiesResourceBundle(translationProperties));

        /*
         * Take the first application source, from behind, as the default one
         */
        final Map<String, CategoryDTO> mergedCategories = createSortedMap(
                categoriesMap.get(repositories.get(numberOfRepositories - 1)), CategoryDTO::getId);

        for (int otherRepositoryIndex = numberOfRepositories - 2; otherRepositoryIndex >= 0; otherRepositoryIndex--) {
            final List<CategoryDTO> otherCategories = categoriesMap.get(repositories.get(otherRepositoryIndex));

            final Map<String, CategoryDTO> otherCategoriesMap = createSortedMap(otherCategories, CategoryDTO::getId);

            for (String categoryId : otherCategoriesMap.keySet()) {
                final CategoryDTO category = otherCategoriesMap.get(categoryId);

                if (mergedCategories.containsKey(categoryId)) {
                    mergedCategories.put(categoryId, mergeCategories(mergedCategories.get(categoryId), category));
                } else {
                    mergedCategories.put(categoryId, category);
                }
            }
        }

        RepositoryDTO mergedRepositoryDTO = repositoryDTOBuilder
                .withCategories(new ArrayList<>(mergedCategories.values())).build();

        return mergedRepositoryDTO;

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
                .withIcon(leftCategory.getIcon()).withId(leftCategory.getId()).withName(leftCategory.getName()).build();
    }

    protected ApplicationDTO mergeApplications(ApplicationDTO leftApplication, ApplicationDTO rightApplication) {
        final List<ScriptDTO> scripts = mergeListOfDtos(leftApplication.getScripts(), rightApplication.getScripts(),
                ScriptDTO::getScriptName, ScriptDTO.nameComparator());
        final List<ResourceDTO> resources = mergeListOfDtos(leftApplication.getResources(),
                rightApplication.getResources(), ResourceDTO::getName, ResourceDTO.nameComparator());

        final List<URI> mergeMiniatures = mergeMiniatures(leftApplication.getMiniatures(),
                rightApplication.getMiniatures());

        return new ApplicationDTO.Builder().withName(leftApplication.getName()).withResources(resources)
                .withScripts(scripts).withDescription(leftApplication.getDescription())
                .withIcon(leftApplication.getIcon()).withMiniatures(mergeMiniatures).build();
    }

    /**
     * Takes two lists of {@link URI}s leading to miniature images and merges them into a single list.
     * During the merging all duplicates are removed, while the miniatures inside <code>leftMiniatures</code> have a higher priority
     *
     * @param leftMiniatures  The first list of miniature {@link URI}s
     * @param rightMiniatures The first list of miniature {@link URI}s
     * @return A list containing the merged miniature {@link URI}s from both <code>leftMiniatures</code> and <code>rightMiniatures</code>
     */
    protected List<URI> mergeMiniatures(List<URI> leftMiniatures, List<URI> rightMiniatures) {
        HashMap<String, URI> mergedMiniatures = new HashMap<>();

        /*
         * Concatenate the both lists with the left list in front of the right one
         */
        List<URI> miniatures = new ArrayList<>(leftMiniatures);
        miniatures.addAll(rightMiniatures);

        /*
         * Remove duplicates
         */
        for (URI miniatureUri : miniatures) {
            try (InputStream inputStream = miniatureUri.toURL().openStream()) {
                String checksum = DigestUtils.md5Hex(inputStream);
                if (!mergedMiniatures.containsKey(checksum)) {
                    mergedMiniatures.put(checksum, miniatureUri);
                }
            } catch (IOException e) {
                LOGGER.error(String.format("Couldn't merge miniatures at %s", miniatureUri.toString()), e);
            }
        }

        return new ArrayList<>(mergedMiniatures.values());
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
