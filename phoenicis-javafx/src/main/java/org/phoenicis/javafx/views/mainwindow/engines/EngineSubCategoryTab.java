package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Tab;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents an engine sub category tab in the tabpane inside the "Engines" section tab.
 * It contains a MiniatureListWidget showing a filtered subset of engine versions belonging to the engine sub category.
 *
 * @author marc
 * @since 22.04.17
 */
public class EngineSubCategoryTab extends Tab {
    private EngineCategoryDTO engineCategory;
    private EngineSubCategoryDTO engineSubCategory;

    private String enginesPath;

    private MiniatureListWidget<EngineVersionDTO> engineVersionsView;

    private ObservableList<EngineVersionDTO> engineVersions;
    private SortedList<EngineVersionDTO> sortedEngineVersions;
    private FilteredList<EngineVersionDTO> filteredEngineVersions;

    private EnginesFilter enginesFilter;

    private Consumer<EngineDTO> onSelectEngine;

    /**
     * Constructor
     *
     * @param engineCategory    The engine category, which contains the engine sub category
     * @param engineSubCategory The engine sub category to be shown in this tab
     * @param enginesPath   The path to the engines
     */
    public EngineSubCategoryTab(EngineCategoryDTO engineCategory, EngineSubCategoryDTO engineSubCategory, String enginesPath) {
        super(engineSubCategory.getDescription());

        this.engineCategory = engineCategory;
        this.engineSubCategory = engineSubCategory;
        this.enginesPath = enginesPath;

        this.enginesFilter = new EnginesFilter(engineSubCategory, enginesPath);

        this.engineVersions = FXCollections.observableArrayList(engineSubCategory.getPackages());
        this.sortedEngineVersions = engineVersions.sorted(EngineSubCategoryDTO.comparator().reversed());
        this.filteredEngineVersions = sortedEngineVersions.filtered(enginesFilter);

        this.populate();

        this.setContent(this.engineVersionsView);
    }

    /**
     * This method populates the engines version miniature list widget and binds the filtered engine versions list to it.
     */
    private void populate() {
        this.engineVersionsView = MiniatureListWidget.create(
                engineVersionDTO -> MiniatureListWidget.Element.create(engineVersionDTO,
                        Files.exists(Paths.get(enginesPath, engineSubCategory.getName(), engineVersionDTO.getVersion()))),
                (engineItem, event) -> {
                    EngineVersionDTO engineVersionDTO = engineItem.getValue();

                    Map<String, String> userData = new HashMap<>();
                    userData.put("Mono", engineVersionDTO.getMonoFile());
                    userData.put("Gecko", engineVersionDTO.getGeckoFile());

                    EngineDTO engineDTO = new EngineDTO.Builder()
                            .withCategory(engineCategory.getName())
                            .withSubCategory(engineSubCategory.getName())
                            .withVersion(engineVersionDTO.getVersion())
                            .withUserData(userData)
                            .build();

                    onSelectEngine.accept(engineDTO);
                });

        Bindings.bindContent(this.engineVersionsView.getItems(), this.filteredEngineVersions);
    }

    /**
     * This method updates the consumer that is called after an engine version has been selected.
     *
     * @param onSelectEngine
     */
    public void setOnSelectEngine(Consumer<EngineDTO> onSelectEngine) {
        this.onSelectEngine = onSelectEngine;
    }

    /**
     * This method updates the filter value if installed engine versions are to be shown in this tab.
     * This method also triggers an automatic update of the view.
     *
     * @param installed The new filter value for installed engine versions
     */
    public void setFilterForInstalled(boolean installed) {
        this.enginesFilter.setShowInstalled(installed);
        this.filteredEngineVersions.setPredicate(enginesFilter::test);
    }

    /**
     * This method updates the filter value if not installed engine versions are to be shown in this tab.
     * This method also triggers an automatic update of the view.
     *
     * @param notInstalled The new filter value for not installed engine versions
     */
    public void setFilterForNotInstalled(boolean notInstalled) {
        this.enginesFilter.setShowNotInstalled(notInstalled);
        this.filteredEngineVersions.setPredicate(enginesFilter::test);
    }

    /**
     * This method updates the search term, which is used to filter for engine versions in this tab.
     * This method also triggers an automatic update of this view.
     *
     * @param searchTerm The new search term to be used for filtering
     */
    public void setFilterForSearchTerm(String searchTerm) {
        this.enginesFilter.setSearchTerm(searchTerm);
        this.filteredEngineVersions.setPredicate(enginesFilter::test);
    }
}
