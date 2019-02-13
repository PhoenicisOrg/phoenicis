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
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.engine.control.EngineInformationPanel;
import org.phoenicis.javafx.components.engine.control.EngineSidebar;
import org.phoenicis.javafx.components.engine.control.EngineSubCategoryPanel;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * "Engines" tab
 */
public class EnginesView extends MainWindowView<EngineSidebar> {
    private final EnginesFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final Map<String, Engine> engines;
    private final ObservableList<EngineCategoryDTO> engineCategories;

    private final ObservableList<Tab> engineSubCategoryTabs;

    private final DetailsPanel engineDetailsPanel;

    private final ObjectProperty<ListWidgetType> selectedListWidget;

    private final ObjectProperty<EngineDTO> engineDTO;

    private final ObjectProperty<Engine> engine;

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

        this.selectedListWidget = new SimpleObjectProperty<>();
        this.engineDTO = new SimpleObjectProperty<>();
        this.engine = new SimpleObjectProperty<>();

        this.filter.selectedEngineCategoryProperty().addListener((Observable invalidation) -> {
            final EngineCategoryDTO engineCategory = this.filter.getSelectedEngineCategory();

            if (engineCategory != null) {
                Optional.ofNullable(onSelectEngineCategory).ifPresent(listener -> listener.accept(engineCategory));
            }
        });

        final EngineSidebar engineSidebar = createEngineSidebar();

        setSidebar(engineSidebar);

        this.engineDetailsPanel = createEngineDetailsPanel();

        this.engineSubCategoryTabs = createEngineSubCategoryTabs(engineSidebar);

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

    private ObservableList<Tab> createEngineSubCategoryTabs(EngineSidebar sidebar) {
        // initialize the engines sub category panels
        final MappedList<List<EngineSubCategoryPanel>, EngineCategoryDTO> engineSubCategoryPanelGroups = new MappedList<>(
                this.engineCategories, engineCategory -> engineCategory.getSubCategories().stream()
                        .map(engineSubCategory -> {
                            final EngineSubCategoryPanel engineSubCategoryPanel = new EngineSubCategoryPanel();

                            engineSubCategoryPanel.setEngineCategory(engineCategory);
                            engineSubCategoryPanel.setEngineSubCategory(engineSubCategory);
                            engineSubCategoryPanel.setEnginesPath(this.enginesPath);
                            engineSubCategoryPanel.setFilter(this.filter);
                            engineSubCategoryPanel.setEngine(this.engines.get(engineCategory.getName().toLowerCase()));

                            engineSubCategoryPanel.selectedListWidgetProperty()
                                    .bind(sidebar.selectedListWidgetProperty());

                            engineSubCategoryPanel.setOnEngineSelect(this::showEngineDetails);

                            return engineSubCategoryPanel;
                        }).collect(Collectors.toList()));

        final ConcatenatedList<EngineSubCategoryPanel> engineSubCategoryPanels = ConcatenatedList
                .create(engineSubCategoryPanelGroups);

        final FilteredList<EngineSubCategoryPanel> filteredEngineSubCategoryPanels = engineSubCategoryPanels
                // sort the engine sub category panels alphabetically
                .sorted(Comparator
                        .comparing(engineSubCategoryPanel -> engineSubCategoryPanel.getEngineSubCategory().getName()))
                // filter the engine sub category panels, so that only the visible panels remain
                .filtered(this.filter::filter);

        filteredEngineSubCategoryPanels.predicateProperty().bind(
                Bindings.createObjectBinding(() -> this.filter::filter,
                        this.filter.searchTermProperty(),
                        this.filter.showInstalledProperty(),
                        this.filter.showNotInstalledProperty()));

        // map the panels to tabs
        return new MappedList<>(filteredEngineSubCategoryPanels,
                engineSubCategoryPanel -> new Tab(engineSubCategoryPanel.getEngineSubCategory().getDescription(),
                        engineSubCategoryPanel));
    }

    private EngineSidebar createEngineSidebar() {
        final EngineSidebar sidebar = new EngineSidebar(this.filter, this.engineCategories, this.selectedListWidget);

        sidebar.setJavaFxSettingsManager(this.javaFxSettingsManager);

        // set the default selection
        sidebar.setSelectedListWidget(javaFxSettingsManager.getEnginesListType());

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                javaFxSettingsManager.setEnginesListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        return sidebar;
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

    private DetailsPanel createEngineDetailsPanel() {
        final EngineInformationPanel engineInformationPanel = new EngineInformationPanel();

        engineInformationPanel.engineDTOProperty().bind(engineDTO);
        engineInformationPanel.engineProperty().bind(engine);

        engineInformationPanel.setOnEngineInstall(this::installEngine);
        engineInformationPanel.setOnEngineDelete(this::deleteEngine);

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings
                .map(engineDTO, engine -> engine.getCategory() + " " + engine.getSubCategory()));
        detailsPanel.setContent(engineInformationPanel);

        detailsPanel.setOnClose(this::closeDetailsView);

        detailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return detailsPanel;
    }

    /**
     * shows details for a given engine
     *
     * @param engineDTO
     */
    private void showEngineDetails(EngineDTO engineDTO, Engine engine) {
        this.engineDTO.setValue(engineDTO);
        this.engine.setValue(engine);

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
