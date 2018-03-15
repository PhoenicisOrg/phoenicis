/**
 *
 */
package org.phoenicis.repository.types;

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

abstract class MergeableRepository implements Repository {
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
        Map<Repository, List<TypeDTO>> typesMap = new HashMap<>();

        for (Map.Entry<Repository, RepositoryDTO> entry : repositoriesMap.entrySet()) {
            RepositoryDTO repositoryDTO = entry.getValue();
            translationProperties.putAll(repositoryDTO.getTranslations().getProperties());
            typesMap.put(entry.getKey(), repositoryDTO.getTypes());
        }
        repositoryDTOBuilder.withTranslations(new TranslationDTO.Builder()
                .withLanguage(Locale.getDefault().getLanguage()).withProperties(translationProperties).build());
        Localisation.setAdditionalTranslations(new PropertiesResourceBundle(translationProperties));

        /*
         * Take the first application source, from behind, as the default one
         */
        final Map<String, TypeDTO> mergedTypes = createSortedMap(
                typesMap.get(repositories.get(numberOfRepositories - 1)), TypeDTO::getId);

        for (int otherRepositoryIndex = numberOfRepositories - 2; otherRepositoryIndex >= 0; otherRepositoryIndex--) {
            final List<TypeDTO> otherTypes = typesMap.get(repositories.get(otherRepositoryIndex));

            final Map<String, TypeDTO> otherTypesMap = createSortedMap(otherTypes, TypeDTO::getId);

            for (Map.Entry<String, TypeDTO> entry : otherTypesMap.entrySet()) {
                final TypeDTO type = entry.getValue();

                if (mergedTypes.containsKey(entry.getKey())) {
                    mergedTypes.put(entry.getKey(),
                            mergeTypes(mergedTypes.get(entry.getKey()), type));
                } else {
                    mergedTypes.put(entry.getKey(), type);
                }
            }
        }

        RepositoryDTO mergedRepositoryDTO = repositoryDTOBuilder
                .withTypes(new ArrayList<>(mergedTypes.values())).build();

        return mergedRepositoryDTO;

    }

    protected TypeDTO mergeTypes(TypeDTO leftCategory, TypeDTO rightCategory) {
        final Map<String, CategoryDTO> leftCategories = createSortedMap(leftCategory.getCategories(),
                CategoryDTO::getId);
        final Map<String, CategoryDTO> rightCategories = createSortedMap(rightCategory.getCategories(),
                CategoryDTO::getId);

        final SortedMap<String, CategoryDTO> mergedCategories = new TreeMap<>(rightCategories);

        for (Map.Entry<String, CategoryDTO> entry : leftCategories.entrySet()) {
            final CategoryDTO category = entry.getValue();

            if (mergedCategories.containsKey(entry.getKey())) {
                mergedCategories.put(entry.getKey(), mergeCategories(mergedCategories.get(entry.getKey()), category));
            } else {
                mergedCategories.put(entry.getKey(), category);
            }
        }

        final List<CategoryDTO> categories = new ArrayList<>(mergedCategories.values());
        categories.sort(CategoryDTO.nameComparator());
        return new TypeDTO.Builder()
                .withId(leftCategory.getId())
                .withName(leftCategory.getName())
                .withCategories(categories)
                .withIcon(leftCategory.getIcon())
                .build();
    }

    protected CategoryDTO mergeCategories(CategoryDTO leftCategory, CategoryDTO rightCategory) {
        final Map<String, ApplicationDTO> leftApplications = createSortedMap(leftCategory.getApplications(),
                ApplicationDTO::getId);
        final Map<String, ApplicationDTO> rightApplications = createSortedMap(rightCategory.getApplications(),
                ApplicationDTO::getId);

        final SortedMap<String, ApplicationDTO> mergedApps = new TreeMap<>(rightApplications);

        for (Map.Entry<String, ApplicationDTO> entry : leftApplications.entrySet()) {
            final ApplicationDTO application = entry.getValue();

            if (mergedApps.containsKey(entry.getKey())) {
                mergedApps.put(entry.getKey(), mergeApplications(mergedApps.get(entry.getKey()), application));
            } else {
                mergedApps.put(entry.getKey(), application);
            }
        }

        final List<ApplicationDTO> applications = new ArrayList<>(mergedApps.values());
        applications.sort(ApplicationDTO.nameComparator());
        return new CategoryDTO.Builder()
                .withTypeId(leftCategory.getTypeId())
                .withId(leftCategory.getId())
                .withName(leftCategory.getName())
                .withApplications(applications)
                .withType(leftCategory.getType())
                .withIcon(leftCategory.getIcon())
                .build();
    }

    protected ApplicationDTO mergeApplications(ApplicationDTO leftApplication, ApplicationDTO rightApplication) {
        final List<ScriptDTO> scripts = mergeListOfDtos(leftApplication.getScripts(), rightApplication.getScripts(),
                ScriptDTO::getScriptName, ScriptDTO.nameComparator());
        final List<ResourceDTO> resources = mergeListOfDtos(leftApplication.getResources(),
                rightApplication.getResources(), ResourceDTO::getName, ResourceDTO.nameComparator());

        final List<URI> mergeMiniatures = mergeMiniatures(leftApplication.getMiniatures(),
                rightApplication.getMiniatures());

        return new ApplicationDTO.Builder()
                .withCategoryId(leftApplication.getCategoryId())
                .withId(leftApplication.getId())
                .withName(leftApplication.getName())
                .withResources(resources)
                .withScripts(scripts)
                .withDescription(leftApplication.getDescription())
                .withIcon(leftApplication.getIcon())
                .withMiniatures(mergeMiniatures)
                .build();
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

        for (Map.Entry<String, T> entry : right.entrySet()) {
            final T dto = entry.getValue();

            if (!merged.containsKey(entry.getKey())) {
                merged.put(entry.getKey(), dto);
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
