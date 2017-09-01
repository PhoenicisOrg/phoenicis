package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.AbstractFilter;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class represents a filter used for filtering the engine versions for
 * <ul>
 * <li>
 * Installed engine versions
 * </li>
 * <li>
 * Not installed engine versions
 * </li>
 * <li>
 * Engine versions containing a search term
 * </li>
 * </ul>
 * This filter depends on a previously defines {@link EngineSubCategoryDTO}.
 *
 * @author marc
 * @since 23.04.17
 */
public class EnginesFilter extends AbstractFilter {
    /**
     * The path to the installed engines
     */
    private String enginesPath;

    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private Optional<String> searchTerm;

    /**
     * Are installed engines searched
     */
    private BooleanProperty showInstalled;

    /**
     * Are not installed engines searched
     */
    private BooleanProperty showNotInstalled;

    /**
     * Constructor
     *
     * @param enginesPath The path to the installed engines
     */
    public EnginesFilter(String enginesPath) {
        super();

        this.enginesPath = enginesPath;

        this.searchTerm = Optional.empty();

        this.showInstalled = new SimpleBooleanProperty();
        this.showInstalled
                .addListener((observableValue, oldValue, newValue) -> this.triggerFilterChanged());

        this.showNotInstalled = new SimpleBooleanProperty();
        this.showNotInstalled
                .addListener((observableValue, oldValue, newValue) -> this.triggerFilterChanged());
    }

    public BooleanProperty showInstalledProperty() {
        return this.showInstalled;
    }

    public BooleanProperty showNotInstalledProperty() {
        return this.showNotInstalled;
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
     * This method checks if a given engine version has been installed
     *
     * @param engineVersionDTO The engine version to be checked
     * @return True if the engine version is installed, false otherwise
     */
    private boolean isInstalled(EngineCategoryDTO engineCategory, EngineSubCategoryDTO engineSubCategory,
            EngineVersionDTO engineVersionDTO) {
        return Paths.get(enginesPath, engineCategory.getName().toLowerCase(), engineSubCategory.getName(),
                engineVersionDTO.getVersion()).toFile().exists();
    }

    /**
     * Creates a new filter predicate for a {@link EngineCategoryDTO} and {@link EngineSubCategoryDTO}.
     * This predicate then accepts a {@link EngineVersionDTO} object and returns true if the given object fulfills the filter predicate and false otherwise
     *
     * @param engineCategory    The engine category
     * @param engineSubCategory The engine sub category
     * @return A new filter predicate
     */
    public Predicate<EngineVersionDTO> createFilter(EngineCategoryDTO engineCategory,
            EngineSubCategoryDTO engineSubCategory) {
        return engineVersion -> {
            final boolean containsSearchTerm = searchTerm
                    .map(searchTerm -> engineVersion.getVersion().toLowerCase().contains(searchTerm.toLowerCase()))
                    .orElse(true);
            final boolean fulfillsShowInstalled = this.showInstalled.getValue()
                    && isInstalled(engineCategory, engineSubCategory, engineVersion);
            final boolean fulfillsShowNotInstalled = this.showNotInstalled.getValue()
                    && !isInstalled(engineCategory, engineSubCategory, engineVersion);

            return containsSearchTerm && (fulfillsShowInstalled || fulfillsShowNotInstalled);
        };
    }

    /**
     * Checks if the given engine category fulfills this filter
     *
     * @param engineCategory The engine category
     * @return True if the given engine category fulfills the filter, false, otherwise
     */
    public boolean filter(EngineCategoryDTO engineCategory) {
        return searchTerm.map(
                searchTerm -> engineCategory.getSubCategories().stream().anyMatch(engineSubCategory -> engineSubCategory
                        .getPackages().stream().anyMatch(version -> version.getVersion().contains(searchTerm))))
                .orElse(true);
    }
}
