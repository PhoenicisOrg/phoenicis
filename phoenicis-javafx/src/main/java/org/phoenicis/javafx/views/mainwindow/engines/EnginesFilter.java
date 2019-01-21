package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.property.*;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;

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
public class EnginesFilter {
    /**
     * The path to the installed engines
     */
    private String enginesPath;

    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private StringProperty searchTerm;

    /**
     * The selected engine category.
     * If no engine category has been selected, this value is {@link Optional#empty()}.
     */
    private ObjectProperty<EngineCategoryDTO> selectedEngineCategory;

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

        this.searchTerm = new SimpleStringProperty("");
        this.selectedEngineCategory = new SimpleObjectProperty<>();

        this.showInstalled = new SimpleBooleanProperty();
        this.showNotInstalled = new SimpleBooleanProperty();
    }

    public EngineCategoryDTO getSelectedEngineCategory() {
        return selectedEngineCategory.get();
    }

    public StringProperty searchTermProperty() {
        return searchTerm;
    }

    public ObjectProperty<EngineCategoryDTO> selectedEngineCategoryProperty() {
        return selectedEngineCategory;
    }

    public BooleanProperty showInstalledProperty() {
        return this.showInstalled;
    }

    public BooleanProperty showNotInstalledProperty() {
        return this.showNotInstalled;
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
     * This predicate then accepts a {@link EngineVersionDTO} object and returns true if the given object fulfills the
     * filter predicate and false otherwise
     *
     * @param engineCategory The engine category
     * @param engineSubCategory The engine sub category
     * @return A new filter predicate
     */
    public Predicate<EngineVersionDTO> createFilter(EngineCategoryDTO engineCategory,
            EngineSubCategoryDTO engineSubCategory) {
        return engineVersion -> {
            final boolean containsSearchTerm = Optional.ofNullable(searchTerm.getValueSafe())
                    .map(searchTerm -> engineVersion.getVersion().toLowerCase().contains(searchTerm.toLowerCase()))
                    .orElse(true);

            final boolean fulfillsShowInstalled = showInstalled.getValue()
                    && isInstalled(engineCategory, engineSubCategory, engineVersion);

            final boolean fulfillsShowNotInstalled = showNotInstalled.getValue()
                    && !isInstalled(engineCategory, engineSubCategory, engineVersion);

            return containsSearchTerm && (fulfillsShowInstalled || fulfillsShowNotInstalled);
        };
    }

    /**
     * Checks if the given engine category fulfills this filter
     *
     * @param engineCategory The engine category
     * @return True if the given engine category fulfills the filter, false otherwise
     */
    public boolean filter(EngineCategoryDTO engineCategory) {
        return Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> engineCategory.getSubCategories().stream()
                        .anyMatch(engineSubCategory -> engineSubCategory.getPackages().stream()
                                .anyMatch(version -> version.getVersion().toLowerCase()
                                        .contains(searchTerm.toLowerCase()))))
                .orElse(true);
    }

    /**
     * Checks if a given engine sub category tab fulfills this filter
     *
     * @param engineSubCategoryTab The engine sub category tab
     * @return True if the given engine sub category tab fulfills the filter, false otherwise
     */
    public boolean filter(EngineSubCategoryTab engineSubCategoryTab) {
        final boolean tabNotEmpty = engineSubCategoryTab.notEmpty();

        final boolean selectedEngineCategoryConstraint = Optional.ofNullable(selectedEngineCategory.getValue())
                .map(selectedEngineCategory -> selectedEngineCategory.equals(engineSubCategoryTab.getEngineCategory()))
                .orElse(true);

        return tabNotEmpty && selectedEngineCategoryConstraint;
    }
}
