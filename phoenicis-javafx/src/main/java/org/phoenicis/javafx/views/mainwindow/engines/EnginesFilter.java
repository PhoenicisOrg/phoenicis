package org.phoenicis.javafx.views.mainwindow.engines;

import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;

import java.nio.file.Files;
import java.nio.file.Paths;
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
public class EnginesFilter implements Predicate<EngineVersionDTO> {
    private EngineCategoryDTO engineCategory;
    private EngineSubCategoryDTO engineSubCategory;
    private String enginesPath;

    /**
     * The search term entered into the search field
     */
    private String searchTerm = "";

    /**
     * Are installed engines searched
     */
    private boolean showInstalled = true;

    /**
     * Are not installed engines searched
     */
    private boolean showNotInstalled = true;

    /**
     * Constructor
     *
     * @param engineSubCategory The engine sub category used for this filter
     * @param enginesPath   The path to the installed engines
     */
    public EnginesFilter(EngineCategoryDTO engineCategory, EngineSubCategoryDTO engineSubCategory, String enginesPath) {
        super();
        this.engineCategory = engineCategory;
        this.engineSubCategory = engineSubCategory;
        this.enginesPath = enginesPath;
    }

    /**
     * This method checks if a given engine version has been installed
     *
     * @param engineVersionDTO The engine version to be checked
     * @return True if the engine version is installed, false otherwise
     */
    private boolean isInstalled(EngineVersionDTO engineVersionDTO) {
        return Paths.get(enginesPath, engineCategory.getName().toLowerCase(), engineSubCategory.getName(),
                engineVersionDTO.getVersion()).toFile().exists();
    }

    @Override
    public boolean test(EngineVersionDTO engineVersion) {
        return engineVersion.getVersion().toLowerCase().contains(searchTerm.toLowerCase())
                && ((this.showInstalled && isInstalled(engineVersion))
                        || (this.showNotInstalled && !isInstalled(engineVersion)));
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void setShowInstalled(boolean showInstalled) {
        this.showInstalled = showInstalled;
    }

    public void setShowNotInstalled(boolean showNotInstalled) {
        this.showNotInstalled = showNotInstalled;
    }
}
