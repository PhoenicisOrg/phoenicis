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

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;

import java.util.List;
import java.util.function.Consumer;

public class ViewEngines extends MainWindowView<EngineSideBar> {
    private EngineSideBar sideBar;

    private TabPane availableEngines;

    private EnginePanel currentEnginePanel;

    private EngineCategoryDTO selectedCategory;

    private ObservableList<EngineCategoryDTO> engineCategories;

    private ObservableList<EngineSubCategoryDTO> engineSubCategories;
    private MappedList<EngineSubCategoryTab, EngineSubCategoryDTO> mappedSubCategoryTabs;

    private Consumer<EngineDTO> setOnInstallEngine = (engine) -> {};
    private Consumer<EngineDTO> setOnDeleteEngine = (engine) -> {};

    private PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

    public ViewEngines(ThemeManager themeManager, String enginesPath) {
        super("Engines", themeManager);

        this.sideBar = new EngineSideBar();

        this.engineCategories = FXCollections.observableArrayList();
        this.engineSubCategories = FXCollections.observableArrayList();
        this.mappedSubCategoryTabs = new MappedList<>(engineSubCategories,
                engineSubCategory -> {
                    EngineSubCategoryTab result = new EngineSubCategoryTab(selectedCategory, engineSubCategory, enginesPath);

                    result.setOnSelectEngine(this::showEngineDetails);

                    return result;
                });

        this.sideBar.setOnCategorySelection(this::selectCategory);
        this.sideBar.setOnApplyInstalledFilter(newValue ->
                availableEngines.getTabs().forEach(tab -> ((EngineSubCategoryTab)tab).setFilterForInstalled(newValue)));
        this.sideBar.setOnApplyUninstalledFilter(newValue ->
                availableEngines.getTabs().forEach(tab -> ((EngineSubCategoryTab)tab).setFilterForNotInstalled(newValue)));
        this.sideBar.setOnSearchTermClear(() ->
                availableEngines.getTabs().forEach(tab -> ((EngineSubCategoryTab)tab).setFilterForSearchTerm("")));
        this.sideBar.setOnApplySearchTerm(this::processFilterText);

        this.sideBar.bindEngineCategories(engineCategories);

        this.initFailure();
        this.initWineVersions();

        Bindings.bindContent(availableEngines.getTabs(), mappedSubCategoryTabs);

        this.setSideBar(sideBar);
        this.showWait();
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

    public void populate(List<EngineCategoryDTO> engineCategoryDTOS) {
        Platform.runLater(() -> {
            this.engineCategories.setAll(engineCategoryDTOS);

            if (!engineCategoryDTOS.isEmpty()) {
                this.sideBar.selectFirstEngineCategory();
            }

            this.clearChronicleNavigateTo(availableEngines);
        });
    }

    public void populateEngines(EngineCategoryDTO category) {
        this.selectedCategory = category;
        this.engineSubCategories.setAll(category.getSubCategories());
    }

    private void selectCategory(EngineCategoryDTO category) {
        this.clearChronicleNavigateTo(availableEngines);
        this.populateEngines(category);
    }

    private void showEngineDetails(EngineDTO engineDTO) {
        currentEnginePanel = new EnginePanel(engineDTO);
        currentEnginePanel.setOnEngineInstall(this::installEngine);
        currentEnginePanel.setOnEngineDelete(this::deleteEngine);

        this.navigateToAtPosition(1, currentEnginePanel);
    }

    private void processFilterText(String filterText) {
        this.pause.setOnFinished(event -> {
            String text = filterText.toLowerCase();

            availableEngines.getTabs().forEach(tab -> ((EngineSubCategoryTab)tab).setFilterForSearchTerm(text));
        });

        this.pause.playFromStart();
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
