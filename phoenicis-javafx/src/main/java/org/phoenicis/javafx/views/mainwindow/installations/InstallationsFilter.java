package org.phoenicis.javafx.views.mainwindow.installations;

import org.phoenicis.javafx.views.AbstractFilter;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

import java.util.Optional;

/**
 * Filter class for the "Installations" tab
 *
 * @author Marc Arndt
 */
public class InstallationsFilter extends AbstractFilter {
    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private Optional<String> searchTerm;

    /**
     * The selected installation category.
     * If no installation category has been selected, this value is {@link Optional#empty()}.
     */
    private Optional<InstallationCategoryDTO> selectedInstallationCategory;

    /**
     * Constructor
     * Assumes an empty search term and no selected installation category
     */
    public InstallationsFilter() {
        super();

        this.searchTerm = Optional.empty();
        this.selectedInstallationCategory = Optional.empty();
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
     * Sets the selected installation category
     * @param installationCategory The installation category, that has been selected
     */
    public void setSelectedInstallationCategory(InstallationCategoryDTO installationCategory) {
        this.selectedInstallationCategory = Optional.ofNullable(installationCategory);

        this.triggerFilterChanged();
    }

    /**
     * Clears both the search term and the selected installation category
     */
    public void clear() {
        this.searchTerm = Optional.empty();
        this.selectedInstallationCategory = Optional.empty();
    }

    /**
     * Filters a given installation category
     * @param installationCategory The to be filtered installation category
     * @return True if the installation category should be shown, false otherwise
     */
    public boolean filter(InstallationCategoryDTO installationCategory) {
        return searchTerm.map(searchTerm -> installationCategory.getInstallations().stream().anyMatch(this::filter))
                .orElse(true);
    }

    /**
     * Filters a given installation
     * @param installation The to be filtered installation
     * @return True if the installation should be shown, false otherwise
     */
    public boolean filter(InstallationDTO installation) {
        return searchTerm.map(searchTerm -> installation.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .orElse(true) &&
                selectedInstallationCategory.map(
                        selectedShortcutCategory -> selectedShortcutCategory.getInstallations().contains(installation))
                        .orElse(true);
    }
}
