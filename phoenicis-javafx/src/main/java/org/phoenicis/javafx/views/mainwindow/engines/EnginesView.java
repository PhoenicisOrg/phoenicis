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
import org.phoenicis.javafx.collections.ExpandedList;
import org.phoenicis.javafx.components.engine.control.EngineDetailsPanel;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
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

    private final FilteredList<EngineSubCategoryTab> engineSubCategoryTabs;

    private final EngineDetailsPanel engineDetailsPanel;

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

        this.filter.selectedEngineCategoryProperty()
                .addListener(invalidation -> Optional.ofNullable(onSelectEngineCategory)
                        .ifPresent(listener -> listener.accept(this.filter.getSelectedEngineCategory())));

        setSidebar(createEnginesSidebar());

        this.engineDetailsPanel = createEngineDetailsPanel();

        this.engineSubCategoryTabs = createEngineSubCategoryTabs();

        this.availableEngines = createEngineVersion();
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

        Bindings.bindContent(availableEngines.getTabs(), this.engineSubCategoryTabs);

        return availableEngines;
    }

    private FilteredList<EngineSubCategoryTab> createEngineSubCategoryTabs() {
        // initialize the engines sub category tabs
        final ExpandedList<EngineSubCategoryTab, EngineCategoryDTO> engineSubCategoryTabs = new ExpandedList<>(
                this.engineCategories,
                engineCategory -> engineCategory
                        .getSubCategories()
                        .stream()
                        .map(engineSubCategory -> {
                            final EngineSubCategoryTab result = new EngineSubCategoryTab(engineCategory,
                                    engineSubCategory,
                                    this.enginesPath, this.filter,
                                    this.engines.get(engineCategory.getName().toLowerCase()),
                                    this.sidebar.selectedListWidgetProperty());

                            result.setOnSelectEngine(this::showEngineDetails);

                            return result;
                        })
                        .collect(Collectors.toList()));

        // sort the engine sub category tabs alphabetically
        // filter the engine sub category tabs, so that only the visible tabs remain
        final FilteredList<EngineSubCategoryTab> filteredEngineSubTabs = engineSubCategoryTabs
                .sorted(Comparator
                        .comparing(engineSubCategoryTab -> engineSubCategoryTab.getEngineSubCategory().getName()))
                .filtered(this.filter::filter);

        filteredEngineSubTabs.predicateProperty().bind(
                Bindings.createObjectBinding(() -> this.filter::filter,
                        this.filter.searchTermProperty(),
                        this.filter.showInstalledProperty(),
                        this.filter.showNotInstalledProperty()));

        return filteredEngineSubTabs;
    }

    private EnginesSidebar createEnginesSidebar() {
        return new EnginesSidebar(this.filter, this.javaFxSettingsManager, this.engineCategories);
    }

    /**
     * inits the view with the given engines
     *
     * @param engineCategoryDTOS
     */
    public void populate(List<EngineCategoryDTO> engineCategoryDTOS, Map<String, Engine> newEngines) {
        this.engines.clear();
        this.engines.putAll(newEngines);

        Platform.runLater(() -> {
            this.engineCategories.setAll(engineCategoryDTOS);

            closeDetailsView();
            setCenter(this.availableEngines);
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

            this.engineCategories.remove(engineCategoryDTO);
            this.engineCategories.add(newEngineCategoryDTO);
        });
    }

    private EngineDetailsPanel createEngineDetailsPanel() {
        final EngineDetailsPanel detailsPanel = new EngineDetailsPanel();

        detailsPanel.setOnClose(this::closeDetailsView);
        detailsPanel.setOnEngineInstall(this::installEngine);
        detailsPanel.setOnEngineDelete(this::deleteEngine);

        detailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return detailsPanel;
    }

    /**
     * shows details for a given engine
     *
     * @param engineDTO
     */
    private void showEngineDetails(EngineDTO engineDTO, Engine engine) {
        engineDetailsPanel.setEngine(engine);
        engineDetailsPanel.setEngineDTO(engineDTO);

        showDetailsView(engineDetailsPanel);
    }

    /**
     * installs given engine
     *
     * @param engineDTO
     */
    private void installEngine(EngineDTO engineDTO) {
        this.setOnInstallEngine.accept(engineDTO);
    }

    /**
     * deletes given engine
     *
     * @param engineDTO
     */
    private void deleteEngine(EngineDTO engineDTO) {
        this.setOnDeleteEngine.accept(engineDTO);
    }

}
