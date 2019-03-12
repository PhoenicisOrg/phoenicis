package org.phoenicis.javafx.components.installation.utils;

import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An utility class for {@link InstallationDTO} objects
 */
public final class InstallationsUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(InstallationsUtils.class);

    /**
     * Constructor
     */
    private InstallationsUtils() {
        // do nothing
    }

    /**
     * Adds a new {@link InstallationDTO} object to an existing list of {@link InstallationDTO} objects
     *
     * @param list  The list of existing installations
     * @param toAdd The new installation
     * @return A new list of installations containing the existing and new installations
     */
    public static List<InstallationCategoryDTO> addInstallationToList(List<InstallationCategoryDTO> list,
                                                                      InstallationDTO toAdd) {
        final String newInstallationCategory = toAdd.getCategory().toString();

        final InstallationCategoryDTO newCategory = new InstallationCategoryDTO.Builder()
                .withId(newInstallationCategory)
                .withName(tr(newInstallationCategory))
                .withInstallations(Collections.singletonList(toAdd))
                .build();

        final SortedMap<String, InstallationCategoryDTO> mergedCategories = new TreeMap<>(
                createSortedMap(list, InstallationCategoryDTO::getId));

        if (mergedCategories.containsKey(newCategory.getId())) {
            final InstallationCategoryDTO tempMergedCategory = mergedCategories.get(newCategory.getId());

            mergedCategories.put(newCategory.getId(), mergeCategories(tempMergedCategory, newCategory));
        } else {
            mergedCategories.put(newCategory.getId(), newCategory);
        }

        return mergedCategories.values().stream()
                .sorted(InstallationCategoryDTO.nameComparator())
                .collect(Collectors.toList());
    }

    /**
     * Removes an {@link InstallationDTO} object from an existing list of {@link InstallationDTO} objects
     *
     * @param list     The list of existing installations
     * @param toRemove The installation which shall be removed
     * @return A new list of installations containing the existing installations without the installation which shall be
     * removed
     */
    public static List<InstallationCategoryDTO> removeInstallationFromList(List<InstallationCategoryDTO> list,
                                                                           InstallationDTO toRemove) {
        final String newInstallationCategory = toRemove.getCategory().toString();

        final SortedMap<String, InstallationCategoryDTO> newCategories = new TreeMap<>(
                createSortedMap(list, InstallationCategoryDTO::getId));

        if (newCategories.containsKey(newInstallationCategory)) {
            final InstallationCategoryDTO mergedCategory = removeFromCategory(
                    newCategories.get(newInstallationCategory),
                    toRemove);
            if (mergedCategory.getInstallations().isEmpty()) {
                newCategories.remove(mergedCategory.getId());
            } else {
                newCategories.replace(mergedCategory.getId(), mergedCategory);
            }
        }

        return newCategories.values().stream()
                .sorted(InstallationCategoryDTO.nameComparator())
                .collect(Collectors.toList());
    }

    private static InstallationCategoryDTO mergeCategories(InstallationCategoryDTO leftCategory,
                                                           InstallationCategoryDTO rightCategory) {
        final Map<String, InstallationDTO> leftInstallations = createSortedMap(leftCategory.getInstallations(),
                InstallationDTO::getId);
        final Map<String, InstallationDTO> rightInstallations = createSortedMap(rightCategory.getInstallations(),
                InstallationDTO::getId);

        final SortedMap<String, InstallationDTO> mergedInstallations = new TreeMap<>(rightInstallations);

        for (Map.Entry<String, InstallationDTO> entry : leftInstallations.entrySet()) {
            final InstallationDTO application = entry.getValue();

            if (mergedInstallations.containsKey(entry.getKey())) {
                LOGGER.error(String.format("Installation %s exists already!", entry.getKey()));
            } else {
                mergedInstallations.put(entry.getKey(), application);
            }
        }

        final List<InstallationDTO> installations = mergedInstallations.values().stream()
                .sorted(InstallationDTO.nameComparator())
                .collect(Collectors.toList());

        return new InstallationCategoryDTO.Builder()
                .withId(leftCategory.getId())
                .withName(leftCategory.getName())
                .withInstallations(installations)
                .withIcon(leftCategory.getIcon())
                .build();
    }

    private static InstallationCategoryDTO removeFromCategory(InstallationCategoryDTO category,
                                                              InstallationDTO newInstallation) {
        final SortedMap<String, InstallationDTO> mergedInstallations = new TreeMap<>(
                createSortedMap(category.getInstallations(), InstallationDTO::getId));

        if (mergedInstallations.containsKey(newInstallation.getId())) {
            mergedInstallations.remove(newInstallation.getId());
        }

        final List<InstallationDTO> installations = mergedInstallations.values().stream()
                .sorted(InstallationDTO.nameComparator())
                .collect(Collectors.toList());

        return new InstallationCategoryDTO.Builder()
                .withId(category.getId())
                .withName(category.getName())
                .withInstallations(installations)
                .withIcon(category.getIcon())
                .build();
    }

    private static <T> Map<String, T> createSortedMap(List<T> dtos, Function<T, String> nameProvider) {
        final SortedMap<String, T> map = new TreeMap<>();

        dtos.forEach(dto -> map.put(nameProvider.apply(dto), dto));

        return map;
    }
}
