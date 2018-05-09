/**
 *
 */
package org.phoenicis.javafx.views.mainwindow.installations;

import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class InstallationsUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(InstallationsUtils.class);

    /**
     * adds a new installation to an existing list of installations
     * @param list existing list of installations
     * @param toAdd new installation
     * @return new list of installations containing the existing and new installations
     */
    public List<InstallationCategoryDTO> addInstallationToList(List<InstallationCategoryDTO> list,
            InstallationDTO toAdd) {
        String newInstallationCategory = toAdd.getCategory().toString();
        final InstallationCategoryDTO newCategory = new InstallationCategoryDTO.Builder()
                .withId(newInstallationCategory)
                .withName(tr(newInstallationCategory))
                .withInstallations(Collections.singletonList(toAdd))
                .build();

        final SortedMap<String, InstallationCategoryDTO> mergedCategories = new TreeMap<>(
                createSortedMap(list, InstallationCategoryDTO::getId));

        if (mergedCategories.containsKey(newCategory.getId())) {
            mergedCategories.put(newCategory.getId(),
                    mergeCategories(mergedCategories.get(newCategory.getId()), newCategory));
        } else {
            mergedCategories.put(newCategory.getId(), newCategory);
        }

        final List<InstallationCategoryDTO> categories = new ArrayList<>(mergedCategories.values());
        categories.sort(InstallationCategoryDTO.nameComparator());
        return categories;
    }

    /**
     * removes as installation from an existing list of installations
     * @param list existing list of installations
     * @param toRemove installation which shall be removed
     * @return new list of installations containing the existing installations without the installation which shall be
     *         removed
     */
    public List<InstallationCategoryDTO> removeInstallationFromList(List<InstallationCategoryDTO> list,
            InstallationDTO toRemove) {
        String newInstallationCategory = toRemove.getCategory().toString();

        final SortedMap<String, InstallationCategoryDTO> newCategories = new TreeMap<>(
                createSortedMap(list, InstallationCategoryDTO::getId));

        if (newCategories.containsKey(newInstallationCategory)) {
            InstallationCategoryDTO mergedCategory = removeFromCategory(newCategories.get(newInstallationCategory),
                    toRemove);
            if (mergedCategory.getInstallations().isEmpty()) {
                newCategories.remove(mergedCategory.getId());
            } else {
                newCategories.replace(mergedCategory.getId(), mergedCategory);
            }
        }

        final List<InstallationCategoryDTO> categories = new ArrayList<>(newCategories.values());
        categories.sort(InstallationCategoryDTO.nameComparator());
        return categories;
    }

    private InstallationCategoryDTO mergeCategories(InstallationCategoryDTO leftCategory,
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

        final List<InstallationDTO> installations = new ArrayList<>(mergedInstallations.values());
        installations.sort(InstallationDTO.nameComparator());
        return new InstallationCategoryDTO.Builder()
                .withId(leftCategory.getId())
                .withName(leftCategory.getName())
                .withInstallations(installations)
                .withIcon(leftCategory.getIcon())
                .build();
    }

    private InstallationCategoryDTO removeFromCategory(InstallationCategoryDTO category,
            InstallationDTO newInstallation) {
        final SortedMap<String, InstallationDTO> mergedInstallations = new TreeMap<>(
                createSortedMap(category.getInstallations(),
                        InstallationDTO::getId));

        if (mergedInstallations.containsKey(newInstallation.getId())) {
            mergedInstallations.remove(newInstallation.getId());
        }

        final List<InstallationDTO> installations = new ArrayList<>(mergedInstallations.values());
        installations.sort(InstallationDTO.nameComparator());
        return new InstallationCategoryDTO.Builder()
                .withId(category.getId())
                .withName(category.getName())
                .withInstallations(installations)
                .withIcon(category.getIcon())
                .build();
    }

    protected <T> Map<String, T> createSortedMap(List<T> dtos, Function<T, String> nameProvider) {
        final SortedMap<String, T> map = new TreeMap<>();
        dtos.forEach(dto -> map.put(nameProvider.apply(dto), dto));
        return map;
    }
}
