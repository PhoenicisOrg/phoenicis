package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Tab;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class represents an engine sub category tab in the tabpane inside the "Engines" section tab.
 * It contains a IconsListWidget showing a filtered subset of engine versions belonging to the engine sub category.
 *
 * @author marc
 * @since 22.04.17
 */
public class EngineSubCategoryTab extends Tab {
    private final EngineCategoryDTO engineCategory;
    private final EngineSubCategoryDTO engineSubCategory;
    private final String enginesPath;
    private final Engine engine;
    private final EnginesFilter filter;

    private final ObservableList<EngineVersionDTO> filteredEngineVersions;

    private final CombinedListWidget<EngineVersionDTO> engineVersionsView;

    private BiConsumer<EngineDTO, Engine> onSelectEngine;

    /**
     * Constructor
     *
     * @param engineCategory The engine category, which contains the engine sub category
     * @param engineSubCategory The engine sub category to be shown in this tab
     * @param enginesPath The path to the engines
     */
    public EngineSubCategoryTab(EngineCategoryDTO engineCategory, EngineSubCategoryDTO engineSubCategory,
            String enginesPath, EnginesFilter filter, Engine engine,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        super(engineSubCategory.getDescription());

        this.engineCategory = engineCategory;
        this.engineSubCategory = engineSubCategory;
        this.enginesPath = enginesPath;
        this.engine = engine;
        this.filter = filter;

        this.filteredEngineVersions = createFilteredEngineVersions();
        this.engineVersionsView = createListWidget(selectedListWidget);

        setContent(engineVersionsView);
    }

    private ObservableList<EngineVersionDTO> createFilteredEngineVersions() {
        final FilteredList<EngineVersionDTO> filteredEngineVersions = FXCollections
                .observableArrayList(engineSubCategory.getPackages())
                .sorted(EngineSubCategoryDTO.comparator().reversed())
                .filtered(filter.createFilter(engineCategory, engineSubCategory));

        filteredEngineVersions.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter.createFilter(engineCategory, engineSubCategory),
                        filter.searchTermProperty(),
                        filter.selectedEngineCategoryProperty(),
                        filter.showInstalledProperty(),
                        filter.showNotInstalledProperty()));

        return filteredEngineVersions;
    }

    private CombinedListWidget<EngineVersionDTO> createListWidget(ObjectProperty<ListWidgetType> selectedListWidget) {
        final ObservableList<ListWidgetEntry<EngineVersionDTO>> listWidgetEntries = new MappedList<>(
                filteredEngineVersions,
                engineVersionDTO -> ListWidgetEntry.create(engineVersionDTO,
                        Files.exists(Paths.get(enginesPath, engineCategory.getName().toLowerCase(),
                                engineSubCategory.getName(), engineVersionDTO.getVersion()))));

        final CombinedListWidget<EngineVersionDTO> listWidget = new CombinedListWidget<>(listWidgetEntries);

        listWidget.selectedListWidgetProperty().bind(selectedListWidget);

        listWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final EngineVersionDTO engineItem = newValue.getItem();

                Map<String, String> userData = new HashMap<>();
                userData.put("Mono", engineItem.getMonoFile());
                userData.put("Gecko", engineItem.getGeckoFile());

                EngineDTO engineDTO = new EngineDTO.Builder()
                        .withCategory(engineCategory.getName())
                        .withSubCategory(engineSubCategory.getName())
                        .withVersion(engineItem.getVersion())
                        .withUserData(userData).build();

                onSelectEngine.accept(engineDTO, engine);
            }
        });

        return listWidget;
    }

    public boolean notEmpty() {
        return engineSubCategory.getPackages().stream()
                .anyMatch(filter.createFilter(engineCategory, engineSubCategory));
    }

    /**
     * This method updates the consumer that is called after an engine version has been selected.
     *
     * @param onSelectEngine
     */
    public void setOnSelectEngine(BiConsumer<EngineDTO, Engine> onSelectEngine) {
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
}
