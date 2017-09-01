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
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.lists.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;

import java.util.List;
import java.util.function.Consumer;

public class EnginesView extends MainWindowView<EnginesSidebar> {
    private final EnginesFilter filter;

    private TabPane availableEngines;

    private EnginePanel currentEnginePanel;

    private EngineCategoryDTO selectedCategory;

    private ObservableList<EngineCategoryDTO> engineCategories;

    private ObservableList<EngineSubCategoryDTO> engineSubCategories;
    private MappedList<EngineSubCategoryTab, EngineSubCategoryDTO> mappedSubCategoryTabs;
    private MappedList<CombinedListWidget<EngineVersionDTO>, EngineSubCategoryTab> mappedListWidgets;

    private Consumer<EngineDTO> setOnInstallEngine = (engine) -> {
    };
    private Consumer<EngineDTO> setOnDeleteEngine = (engine) -> {
    };

    public EnginesView(ThemeManager themeManager, String enginesPath, JavaFxSettingsManager javaFxSettingsManager) {
        super("Engines", themeManager);

        this.filter = new EnginesFilter(enginesPath);

        this.engineCategories = FXCollections.observableArrayList();
        this.engineSubCategories = FXCollections.observableArrayList();
        this.mappedSubCategoryTabs = new MappedList<>(engineSubCategories, engineSubCategory -> {
            EngineSubCategoryTab result = new EngineSubCategoryTab(selectedCategory, engineSubCategory, enginesPath,
                    filter);

            result.setOnSelectEngine(this::showEngineDetails);

            return result;
        });
        this.mappedListWidgets = new MappedList<>(mappedSubCategoryTabs, tab -> tab.getEngineVersionsView());

        this.sidebar = new EnginesSidebar(mappedListWidgets, filter, javaFxSettingsManager);

        this.sidebar.setOnCategorySelection(this::selectCategory);

        this.initFailure();
        this.initWineVersions();

        this.sidebar.bindEngineCategories(engineCategories);
        Bindings.bindContent(availableEngines.getTabs(), mappedSubCategoryTabs);

        this.setSidebar(this.sidebar);
    }

    public void setOnInstallEngine(Consumer<EngineDTO> onInstallEngine) {
        this.setOnInstallEngine = onInstallEngine;
    }

    public void setOnDeleteEngine(Consumer<EngineDTO> onDeleteEngine) {
        this.setOnDeleteEngine = onDeleteEngine;
    }

    private void initFailure() {

    }

    private void initWineVersions() {
        availableEngines = new TabPane();
        availableEngines.getStyleClass().add("rightPane");

        availableEngines.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    // TODO: delete this method because it doesn't do what it promises, namely showing the wine versions tab
    @Deprecated
    public void showWineVersions() {
        setCenter(availableEngines);
    }

    public void populate(List<EngineCategoryDTO> engineCategoryDTOS) {
        Platform.runLater(() -> {
            this.engineCategories.setAll(engineCategoryDTOS);

            if (!engineCategoryDTOS.isEmpty()) {
                this.sidebar.selectFirstEngineCategory();
            }

            this.closeDetailsView();
            this.setCenter(availableEngines);
        });
    }

    public void populateEngines(EngineCategoryDTO category) {
        this.selectedCategory = category;
        this.engineSubCategories.setAll(category.getSubCategories());
    }

    private void selectCategory(EngineCategoryDTO category) {
        this.setCenter(availableEngines);
        this.populateEngines(category);
    }

    private void showEngineDetails(EngineDTO engineDTO) {
        currentEnginePanel = new EnginePanel(engineDTO);
        currentEnginePanel.setOnClose(this::closeDetailsView);
        currentEnginePanel.setOnEngineInstall(this::installEngine);
        currentEnginePanel.setOnEngineDelete(this::deleteEngine);

        this.showDetailsView(currentEnginePanel);
    }

    private void installEngine(EngineDTO engineDTO) {
        this.setOnInstallEngine.accept(engineDTO);
    }

    private void deleteEngine(EngineDTO engineDTO) {
        this.setOnDeleteEngine.accept(engineDTO);
    }

    public void showProgress(VBox progressUi) {
        currentEnginePanel.showProgress(progressUi);
    }
}
