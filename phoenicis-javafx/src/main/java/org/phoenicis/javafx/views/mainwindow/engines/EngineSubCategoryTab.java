package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Tab;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class represents an engine sub category tab in the tabpane inside the "Engines" section tab.
 * It contains a IconsListWidget showing a filtered subset of engine versions belonging to the engine sub category.
 *
 * @author marc
 * @since 22.04.17
 */
public class EngineSubCategoryTab extends Tab {
    private EngineCategoryDTO engineCategory;
    private EngineSubCategoryDTO engineSubCategory;
    private String enginesPath;

    private Predicate<EngineVersionDTO> filterPredicate;

    private CombinedListWidget<EngineVersionDTO> engineVersionsView;

    private ObservableList<EngineVersionDTO> engineVersions;
    private SortedList<EngineVersionDTO> sortedEngineVersions;
    private PhoenicisFilteredList<EngineVersionDTO> filteredEngineVersions;

    private Consumer<EngineDTO> onSelectEngine;

    /**
     * Constructor
     *
     * @param engineCategory    The engine category, which contains the engine sub category
     * @param engineSubCategory The engine sub category to be shown in this tab
     * @param enginesPath   The path to the engines
     */
    public EngineSubCategoryTab(EngineCategoryDTO engineCategory, EngineSubCategoryDTO engineSubCategory,
            String enginesPath, EnginesFilter filter) {
        super(engineSubCategory.getDescription());

        this.engineCategory = engineCategory;
        this.engineSubCategory = engineSubCategory;
        this.enginesPath = enginesPath;

        this.filterPredicate = filter.createFilter(engineCategory, engineSubCategory);

        this.engineVersions = FXCollections.observableArrayList(engineSubCategory.getPackages());
        this.sortedEngineVersions = engineVersions.sorted(EngineSubCategoryDTO.comparator().reversed());
        this.filteredEngineVersions = new PhoenicisFilteredList<>(sortedEngineVersions, filterPredicate);
        // TODO: when the sub category tab isn't needed anymore the filter changed trigger isn't removed
        filter.addOnFilterChanged(filteredEngineVersions::trigger);

        this.populate();

        this.setContent(this.engineVersionsView);
    }

    /**
     * This method populates the engines version miniature list widget and binds the filtered engine versions list to it.
     */
    private void populate() {
        this.engineVersionsView = new CombinedListWidget<>(
                engineVersionDTO -> ListWidgetEntry
                        .create(engineVersionDTO,
                                Files.exists(Paths.get(enginesPath, engineCategory.getName().toLowerCase(),
                                        engineSubCategory.getName(), engineVersionDTO.getVersion()))),
                (engineItem, event) -> {
                    EngineVersionDTO engineVersionDTO = engineItem;

                    Map<String, String> userData = new HashMap<>();
                    userData.put("Mono", engineVersionDTO.getMonoFile());
                    userData.put("Gecko", engineVersionDTO.getGeckoFile());

                    EngineDTO engineDTO = new EngineDTO.Builder().withCategory(engineCategory.getName())
                            .withSubCategory(engineSubCategory.getName()).withVersion(engineVersionDTO.getVersion())
                            .withUserData(userData).build();

                    onSelectEngine.accept(engineDTO);
                });

        this.engineVersionsView.bind(filteredEngineVersions);
    }

    /**
     * This method updates the consumer that is called after an engine version has been selected.
     *
     * @param onSelectEngine
     */
    public void setOnSelectEngine(Consumer<EngineDTO> onSelectEngine) {
        this.onSelectEngine = onSelectEngine;
    }

    public CombinedListWidget<EngineVersionDTO> getEngineVersionsView() {
        return engineVersionsView;
    }

    public EngineCategoryDTO getEngineCategory() {
        return this.engineCategory;
    }

    public EngineSubCategoryDTO getEngineSubCategory() {
        return this.engineSubCategory;
    }

    public Predicate<EngineVersionDTO> getFilterPredicate() {
        return this.filterPredicate;
    }
}
