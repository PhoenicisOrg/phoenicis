package org.phoenicis.javafx.views.mainwindow.engines;

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
 * Uninstalled engine versions
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
    private EngineSubCategoryDTO engineSubCategory;

    private String wineEnginesPath;

    /**
     * The search term entered into the search field
     */
    private String searchTerm = "";

    /**
     * Are only installed engines searched
     */
    private boolean onlyInstalled = true;

    /**
     * Are only not installed engines searched
     */
    private boolean onlyUninstalled = true;

    /**
     * Constructor
     *
     * @param engineSubCategory The engine sub category used for this filter
     * @param wineEnginesPath   The path to the installed wine engines
     */
    public EnginesFilter(EngineSubCategoryDTO engineSubCategory, String wineEnginesPath) {
        super();

        this.engineSubCategory = engineSubCategory;
        this.wineEnginesPath = wineEnginesPath;
    }

    /**
     * This method checks if a given engine version has been installed
     *
     * @param engineVersionDTO The engine version to be checked
     * @return True if the engine version is installed, false otherwise
     */
    private boolean isInstalled(EngineVersionDTO engineVersionDTO) {
        return Files.exists(Paths.get(wineEnginesPath, engineSubCategory.getName(), engineVersionDTO.getVersion()));
    }

    @Override
    public boolean test(EngineVersionDTO engineVersion) {
        return engineVersion.getVersion().toLowerCase().contains(searchTerm.toLowerCase()) &&
                ((this.onlyInstalled && isInstalled(engineVersion)) || (this.onlyUninstalled && !isInstalled(engineVersion)));
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void setOnlyInstalled(boolean onlyInstalled) {
        this.onlyInstalled = onlyInstalled;
    }

    public void setOnlyUninstalled(boolean onlyUninstalled) {
        this.onlyUninstalled = onlyUninstalled;
    }
}
