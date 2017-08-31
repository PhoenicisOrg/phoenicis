package org.phoenicis.javafx.views.mainwindow.containers;

import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.views.AbstractFilter;

import java.util.Optional;

/**
 * Filter class for the "Containers" tab
 *
 * @author Marc Arndt
 */
public class ContainersFilter extends AbstractFilter {
    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private Optional<String> searchTerm;

    /**
     * The selected container category.
     * If no container category has been selected, this value is {@link Optional#empty()}.
     */
    private Optional<ContainerCategoryDTO> selectedContainerCategory;

    /**
     * Constructor
     * Assumes an empty search term and no selected container category
     */
    public ContainersFilter() {
        super();

        this.searchTerm = Optional.empty();
        this.selectedContainerCategory = Optional.empty();
    }

    /**
     * Sets the search term to the given string.
     *
     * @param searchTerm The new search term
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = Optional.of(searchTerm);

        this.triggerFilterChanged();
    }

    /**
     * Clears the search term
     */
    public void clearSearchTerm() {
        this.searchTerm = Optional.empty();

        this.triggerFilterChanged();
    }

    /**
     * Sets the selected container category
     * @param containerCategory The container category, that has been selected
     */
    public void setSelectedContainerCategory(ContainerCategoryDTO containerCategory) {
        this.selectedContainerCategory = Optional.ofNullable(containerCategory);

        this.triggerFilterChanged();
    }

    /**
     * Clears both the search term and the selected container category
     */
    public void clear() {
        this.searchTerm = Optional.empty();
        this.selectedContainerCategory = Optional.empty();
    }

    /**
     * Filters a given container category
     * @param containerCategory The to be filtered container category
     * @return True if the container category should be shown, false otherwise
     */
    public boolean filter(ContainerCategoryDTO containerCategory) {
        return searchTerm.map(searchTerm -> containerCategory.getContainers().stream().anyMatch(this::filter))
                .orElse(true);
    }

    /**
     * Filters a given container
     * @param installation The to be filtered container
     * @return True if the container should be shown, false otherwise
     */
    public boolean filter(ContainerDTO installation) {
        return searchTerm.map(searchTerm -> installation.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .orElse(true) &&
                selectedContainerCategory.map(
                        selectedContainerCategory -> selectedContainerCategory.getContainers().contains(installation))
                        .orElse(true);
    }
}
