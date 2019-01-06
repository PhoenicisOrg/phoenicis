package org.phoenicis.javafx.views.mainwindow.installations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

import java.util.Optional;

/**
 * Filter class for the "Installations" tab
 *
 * @author Marc Arndt
 */
public class InstallationsFilter {
    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private StringProperty searchTerm;

    /**
     * The selected installation category.
     * If no installation category has been selected, this value is {@link Optional#empty()}.
     */
    private ObjectProperty<InstallationCategoryDTO> selectedInstallationCategory;

    /**
     * Constructor
     * Assumes an empty search term and no selected installation category
     */
    public InstallationsFilter() {
        super();

        this.searchTerm = new SimpleStringProperty("");
        this.selectedInstallationCategory = new SimpleObjectProperty<>();
    }

    /**
     * Filters a given installation category
     *
     * @param installationCategory The to be filtered installation category
     * @return True if the installation category should be shown, false otherwise
     */
    public boolean filter(InstallationCategoryDTO installationCategory) {
        return Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> installationCategory.getInstallations().stream().anyMatch(this::filter))
                .orElse(true);
    }

    /**
     * Filters a given installation
     *
     * @param installation The to be filtered installation
     * @return True if the installation should be shown, false otherwise
     */
    public boolean filter(InstallationDTO installation) {
        final boolean searchTermConstraint = Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> installation.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .orElse(true);

        final boolean selectedInstallationCategoryConstraint = Optional
                .ofNullable(selectedInstallationCategory.getValue())
                .map(selectedShortcutCategory -> selectedShortcutCategory.getInstallations().contains(installation))
                .orElse(true);

        return searchTermConstraint && selectedInstallationCategoryConstraint;

    }

    public StringProperty searchTermProperty() {
        return searchTerm;
    }

    public ObjectProperty<InstallationCategoryDTO> selectedInstallationCategoryProperty() {
        return selectedInstallationCategory;
    }
}
