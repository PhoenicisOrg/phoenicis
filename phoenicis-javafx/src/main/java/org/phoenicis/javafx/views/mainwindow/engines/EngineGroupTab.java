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
 * Created by marc on 22.04.17.
 */
public class EngineGroupTab extends Tab {
    private EngineCategoryDTO engineCategory;
    private EngineSubCategoryDTO engineSubCategory;

    private String wineEnginesPath;

    private MiniatureListWidget<EngineVersionDTO> engineVersionsView;

    private ObservableList<EngineVersionDTO> engineVersions;
    private FilteredList<EngineVersionDTO> filteredEngineVersions;
    private SortedList<EngineVersionDTO> sortedEngineVersions;

    private Consumer<EngineDTO> onSelectEngine;

    public EngineGroupTab(EngineCategoryDTO engineCategory, EngineSubCategoryDTO engineSubCategory, String wineEnginesPath) {
        super(engineSubCategory.getDescription());

        this.engineCategory = engineCategory;
        this.engineSubCategory = engineSubCategory;
        this.wineEnginesPath = wineEnginesPath;

        this.engineVersions = FXCollections.observableArrayList(engineSubCategory.getPackages());
        this.filteredEngineVersions = engineVersions.filtered(engineVersion -> true);
        this.sortedEngineVersions = filteredEngineVersions.sorted(EngineSubCategoryDTO.comparator().reversed());

        this.populate();

        this.setContent(this.engineVersionsView);
    }

    private void populate() {
        this.engineVersionsView = MiniatureListWidget.create(
                engineVersionDTO -> MiniatureListWidget.Element.create(engineVersionDTO,
                        Files.exists(Paths.get(wineEnginesPath, engineSubCategory.getName(), engineVersionDTO.getVersion()))),
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

        Bindings.bindContent(this.engineVersionsView.getItems(), this.sortedEngineVersions);
    }

    public void setOnSelectEngine(Consumer<EngineDTO> onSelectEngine) {
        this.onSelectEngine = onSelectEngine;
    }
}
