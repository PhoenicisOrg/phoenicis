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
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.phoenicis.engines.CombinedEnginesFilter;
import org.phoenicis.engines.EnginesFilter;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewEngines extends MainWindowView {

    private class CheckBoxListener implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
            BooleanProperty selectedProperty = (BooleanProperty) observableValue;
            CheckBox checkBox = (CheckBox) selectedProperty.getBean();
            if (newValue) {
                currentFilter.add((EnginesFilter) checkBox.getUserData());
            } else {
                currentFilter.remove((EnginesFilter) checkBox.getUserData());
            }
            onApplyFilter.accept(currentFilter);
        }
    }

    private TabPane availableEngines;
    private EnginePanel currentEnginePanel;
    private LeftGroup categoryView;
    private final CombinedEnginesFilter currentFilter = new CombinedEnginesFilter();
    private Consumer<CombinedEnginesFilter> onApplyFilter = (filter) -> {};
    private Consumer<EngineCategoryDTO> onSelectCategory;
    private Consumer<EngineDTO> setOnInstallEngine = (engine) -> {};
    private Consumer<EngineDTO> setOnDeleteEngine = (engine) -> {};

    public ViewEngines(ThemeManager themeManager) {
        super("Engines", themeManager);

        //   entitiesProvider = eventHandlerLibrary.getRemoteWineVersions();
        currentFilter.add(EnginesFilter.INSTALLED);
        currentFilter.add(EnginesFilter.NOT_INSTALLED);

        this.initFailure();
        this.initWineVersions();

        this.drawSideBar();
        this.showWait();
    }

    public void setOnApplyFilter(Consumer<CombinedEnginesFilter> onApplyFilter) {
        this.onApplyFilter = onApplyFilter;
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

    public void showWineVersions() {
        showRightView(availableEngines);
    }

    @Override
    protected void drawSideBar() {
        super.drawSideBar();

        final SearchBox searchBar = new SearchBox(themeManager, filterText -> {}, () -> {});
        searchBar.setOnKeyReleased(event -> {});

        categoryView = new LeftGroup(translate("Engines"));

        LeftSpacer spacer = new LeftSpacer();

        final CheckBox installedCheck = new LeftCheckBox(translate("Installed"));
        installedCheck.setUserData(EnginesFilter.INSTALLED);
        installedCheck.setSelected(true);
        installedCheck.selectedProperty().addListener(new CheckBoxListener());
        final CheckBox notInstalledCheck = new LeftCheckBox(translate("Not installed"));
        notInstalledCheck.setUserData(EnginesFilter.NOT_INSTALLED);
        notInstalledCheck.setSelected(true);
        notInstalledCheck.selectedProperty().addListener(new CheckBoxListener());

        addToSideBar(searchBar, spacer, categoryView, new LeftSpacer(), installedCheck, notInstalledCheck);
    }

    public void setUpEvents() {
        //entitiesProvider.setOnChange(this::update);
    }


    public void populate(List<EngineCategoryDTO> engineCategoryDTOS, String wineEnginesPath) {
        Platform.runLater(() -> {
            final List<LeftButton> leftButtonList = new ArrayList<>();
            for (EngineCategoryDTO category : engineCategoryDTOS) {
                final LeftButton categoryButton = new LeftButton(category.getName());
                final String resource = String.format("icons/mainwindow/engines/%s.png", category.getName().toLowerCase());
                if (themeManager.resourceExists(resource)) {
                    categoryButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(resource) + "');");
                } else {
                    categoryButton.setStyle("-fx-background-image: url('" + category.getIcon() + "');");
                }
                categoryButton.setOnMouseClicked(event -> selectCategory(category));
                leftButtonList.add(categoryButton);
            }

            categoryView.setNodes(leftButtonList);
            showAvailableEngines();
        });
    }

    public void populateEngines(String category, List<EngineSubCategoryDTO> subCategories, String wineEnginesPath) {
        availableEngines.getTabs().clear();

        for (EngineSubCategoryDTO subCategory : subCategories) {
            final MiniatureListWidget<EngineVersionDTO> tabContent = MiniatureListWidget.create(engineVersionDTO -> {
                File f = new File(wineEnginesPath + "/" + subCategory.getName() + "/" + engineVersionDTO.getVersion());

                return MiniatureListWidget.Element.create(engineVersionDTO, f.exists());
            },(engineItem, event) -> {
                EngineVersionDTO engineVersionDTO = engineItem.getValue();
                
                Map<String, String> userData = new HashMap<>();
                userData.put("Mono", engineVersionDTO.getMonoFile());
                userData.put("Gecko", engineVersionDTO.getGeckoFile());
                EngineDTO engineDTO = new EngineDTO.Builder()
                        .withCategory(category)
                        .withSubCategory(subCategory.getName())
                        .withVersion(engineVersionDTO.getVersion())
                        .withUserData(userData)
                        .build();

                this.showEngineDetails(engineDTO);
            });

            List<EngineVersionDTO> packages = subCategory.getPackages();
            packages.sort(EngineSubCategoryDTO.comparator().reversed());

            tabContent.setItems(packages);

            availableEngines.getTabs().add(new Tab(subCategory.getDescription(), tabContent));
        }
    }

    public void showAvailableEngines() {
        showRightView(availableEngines);
    }

    private void selectCategory(EngineCategoryDTO category) {
        showRightView(availableEngines);
        this.onSelectCategory.accept(category);
    }

    private void showEngineDetails(EngineDTO engineDTO) {
        currentEnginePanel = new EnginePanel(engineDTO);
        currentEnginePanel.setOnEngineInstall(this::installEngine);
        currentEnginePanel.setOnEngineDelete(this::deleteEngine);
        showRightView(currentEnginePanel);
    }

    public void setOnSelectCategory(Consumer<EngineCategoryDTO> onSelectCategory) {
        this.onSelectCategory = onSelectCategory;
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
