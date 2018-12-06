/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TabPane;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.lists.ExpandedList;
import org.phoenicis.javafx.views.common.lists.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;


import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * "Engines" tab
 */
public class EnginesView extends MainWindowView<EnginesSidebar> {
    private final EnginesFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final Map<String, Engine> engines;
    private final ObservableList<EngineCategoryDTO> engineCategories;

    private TabPane availableEngines;

    private Consumer<EngineDTO> setOnInstallEngine;
    private Consumer<EngineDTO> setOnDeleteEngine;
    private Consumer<EngineCategoryDTO> onSelectEngineCategory;

    private String enginesPath;

    /**
     * constructor
     *
     * @param themeManager
     * @param enginesPath
     * @param javaFxSettingsManager
     */
    public EnginesView(ThemeManager themeManager, String enginesPath, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Engines"), themeManager);

        this.enginesPath = enginesPath;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.filter = new EnginesFilter(enginesPath);
        this.engines = new HashMap<>();
        this.engineCategories = FXCollections.observableArrayList();

        filter.selectedEngineCategoryProperty().addListener(invalidation -> Optional.ofNullable(onSelectEngineCategory)
                .ifPresent(listener -> listener.accept(filter.getSelectedEngineCategory())));

        this.availableEngines = createEngineVersion();

        setSidebar(createEnginesSidebar());
    }

    /**
     * sets the consumer which shall be executed if an engine is selected
     *
     * @param engineCategory
     */
    public void setOnSelectEngineCategory(Consumer<EngineCategoryDTO> engineCategory) {
        this.onSelectEngineCategory = engineCategory;
    }

    /**
     * sets the consumer which shall be executed if an engine is installed
     *
     * @param onInstallEngine
     */
    public void setOnInstallEngine(Consumer<EngineDTO> onInstallEngine) {
        this.setOnInstallEngine = onInstallEngine;
    }

    /**
     * sets the consumer which shall be executed if an engine is deleted
     *
     * @param onDeleteEngine
     */
    public void setOnDeleteEngine(Consumer<EngineDTO> onDeleteEngine) {
        this.setOnDeleteEngine = onDeleteEngine;
    }

    private TabPane createEngineVersion() {
        final TabPane availableEngines = new TabPane();

        availableEngines.getStyleClass().add("rightPane");
        availableEngines.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        return availableEngines;
    }

    private EnginesSidebar createEnginesSidebar() {
        // initialize the engines sub category tabs
        final ExpandedList<EngineSubCategoryTab, EngineCategoryDTO> engineSubCategoryTabs = new ExpandedList<>(
                engineCategories,
                engineCategory -> engineCategory
                        .getSubCategories()
                        .stream()
                        .map(engineSubCategory -> {
                            EngineSubCategoryTab result = new EngineSubCategoryTab(engineCategory, engineSubCategory,
                                    enginesPath,
                                    filter,
                                    engines.get(engineCategory.getName().toLowerCase()));

                            result.setOnSelectEngine(this::showEngineDetails);

                            return result;
                        })
                        .collect(Collectors.toList()));

        // sort the engine sub category tabs alphabetically
        // filter the engine sub category tabs, so that only the visible tabs remain
        final FilteredList<EngineSubCategoryTab> filteredEngineSubTabs = engineSubCategoryTabs
                .sorted(Comparator
                        .comparing(engineSubCategoryTab -> engineSubCategoryTab.getEngineSubCategory().getName()))
                .filtered(filter::filter);

        filteredEngineSubTabs.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter,
                        filter.searchTermProperty(),
                        filter.showInstalledProperty(),
                        filter.showNotInstalledProperty()));

        final MappedList<CombinedListWidget<EngineVersionDTO>, EngineSubCategoryTab> mappedListWidgets = new MappedList<>(
                filteredEngineSubTabs, EngineSubCategoryTab::getEngineVersionsView);

        return new EnginesSidebar(filter, javaFxSettingsManager, engineCategories, mappedListWidgets);
    }

    /**
     * inits the view with the given engines
     *
     * @param engineCategoryDTOS
     */
    public void populate(List<EngineCategoryDTO> engineCategoryDTOS, Map<String, Engine> newEngines) {
        engines.clear();
        engines.putAll(newEngines);

        Platform.runLater(() -> {
            engineCategories.setAll(engineCategoryDTOS);

            closeDetailsView();
            setCenter(availableEngines);
        });
    }

    /**
     * updates available versions for a certain engine
     *
     * @param engineCategoryDTO engine
     * @param versions available versions for the engine
     */
    public void updateVersions(EngineCategoryDTO engineCategoryDTO, List<EngineSubCategoryDTO> versions) {
        Platform.runLater(() -> {
            EngineCategoryDTO newEngineCategoryDTO = new EngineCategoryDTO.Builder(engineCategoryDTO)
                    .withSubCategories(versions)
                    .build();

            engineCategories.remove(engineCategoryDTO);
            engineCategories.add(newEngineCategoryDTO);
        });
    }

    /**
     * shows details for a given engine
     *
     * @param engineDTO
     */
    private void showEngineDetails(EngineDTO engineDTO, Engine engine) {
        final EnginePanel currentEnginePanel = new EnginePanel(engineDTO, engine);

        currentEnginePanel.setOnClose(this::closeDetailsView);
        currentEnginePanel.setOnEngineInstall(this::installEngine);
        currentEnginePanel.setOnEngineDelete(this::deleteEngine);
        currentEnginePanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));

        showDetailsView(currentEnginePanel);
    }

    /**
     * installs given engine
     *
     * @param engineDTO
     */
    private void installEngine(EngineDTO engineDTO) {
        setOnInstallEngine.accept(engineDTO);
    }

    /**
     * deletes given engine
     *
     * @param engineDTO
     */
    private void deleteEngine(EngineDTO engineDTO) {
        setOnDeleteEngine.accept(engineDTO);
    }

}
