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

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import org.phoenicis.engines.CombinedEnginesFilter;
import org.phoenicis.engines.EnginesFilter;
import org.phoenicis.engines.dto.WineVersionDTO;
import org.phoenicis.engines.dto.WineVersionDistributionDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.common.widget.StaticMiniature;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.ui.LeftBarTitle;
import org.phoenicis.javafx.views.mainwindow.ui.LeftButton;
import org.phoenicis.javafx.views.mainwindow.ui.LeftCheckBox;
import org.phoenicis.javafx.views.mainwindow.ui.LeftSpacer;

import java.io.File;
import java.util.List;
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

    private TabPane wineDistributionsTabPane;
    private final CombinedEnginesFilter currentFilter = new CombinedEnginesFilter();
    private Consumer<CombinedEnginesFilter> onApplyFilter = (filter) -> {};
    private Consumer<WineVersionDTO> setOnInstallEngine = (engine) -> {};
    private Consumer<WineVersionDTO> setOnDeleteEngine = (engine) -> {};

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
    public void setOnInstallEngine(Consumer<WineVersionDTO> onInstallEngine) {
        this.setOnInstallEngine = onInstallEngine;
    }
    public void setOnDeleteEngine(Consumer<WineVersionDTO> onDeleteEngine) {
        this.setOnDeleteEngine = onDeleteEngine;
    }

    private void initFailure() {

    }

    private void initWineVersions() {
        wineDistributionsTabPane = new TabPane();
        wineDistributionsTabPane.getStyleClass().add("rightPane");

        wineDistributionsTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    public void showWineVersions() {
        showRightView(wineDistributionsTabPane);
    }

    @Override
    protected void drawSideBar() {
        super.drawSideBar();

        final TextField searchBar = new TextField();
        searchBar.getStyleClass().add("searchBar");
        searchBar.setOnKeyReleased(event -> {
        });

        LeftButton wine = new LeftButton("Wine");
        final String wineButtonIcon = "icons/mainwindow/engines/wine.png";
        wine.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(wineButtonIcon) + "');");
        wine.setOnMouseClicked(event -> showWineVersions());

        LeftSpacer spacer = new LeftSpacer();

        final CheckBox installedCheck = new LeftCheckBox(translate("Installed"));
        installedCheck.setUserData(EnginesFilter.INSTALLED);
        installedCheck.setSelected(true);
        installedCheck.selectedProperty().addListener(new CheckBoxListener());
        final CheckBox notInstalledCheck = new LeftCheckBox(translate("Not installed"));
        notInstalledCheck.setUserData(EnginesFilter.NOT_INSTALLED);
        notInstalledCheck.setSelected(true);
        notInstalledCheck.selectedProperty().addListener(new CheckBoxListener());

        addToSideBar(searchBar, spacer, new LeftBarTitle("Engines"), wine, new LeftSpacer(), installedCheck, notInstalledCheck);
    }

    public void setUpEvents() {
        //entitiesProvider.setOnChange(this::update);
    }


    public void populate(List<WineVersionDistributionDTO> wineVersionDistributionDTOs, String wineEnginesPath) {
        wineDistributionsTabPane.getTabs().clear();
        for (WineVersionDistributionDTO wineVersionDistributionDTO : wineVersionDistributionDTOs) {
            wineDistributionsTabPane.getTabs().add(createWineDistributionTab(wineVersionDistributionDTO, wineEnginesPath));
        }
    }

    private Tab createWineDistributionTab(WineVersionDistributionDTO wineVersionDistributionDTO, String wineEnginesPath) {
        final MiniatureListWidget tabContent = MiniatureListWidget.create();
        List<WineVersionDTO> packages = wineVersionDistributionDTO.getPackages();
        packages.sort(WineVersionDistributionDTO.comparator().reversed());

        for (WineVersionDTO wineVersionDTO :
                packages) {
            final Node engineItem = tabContent.addItem(wineVersionDTO.getVersion(), new StaticMiniature(StaticMiniature.WINE_MINIATURE));
            // gray scale if not installed
            File f = new File(wineEnginesPath + "/" + wineVersionDistributionDTO.getName() + "/" + wineVersionDTO.getVersion());
            if(!f.exists()) {
                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(-1);
                engineItem.setEffect(grayscale);
            }
            engineItem.setOnMouseClicked(event -> this.showEngineDetails(wineVersionDTO));
        }

        return new Tab(wineVersionDistributionDTO.getDescription(), tabContent);
    }

    private void showEngineDetails(WineVersionDTO wineVersionDTO) {
        final EnginePanel enginePanel = new EnginePanel(wineVersionDTO);
        enginePanel.setOnEngineInstall(this::installEngine);
        enginePanel.setOnEngineDelete(this::deleteEngine);
        showRightView(enginePanel);
    }

    private void installEngine(WineVersionDTO wineVersionDTO) {
        this.setOnInstallEngine.accept(wineVersionDTO);
    }

    private void deleteEngine(WineVersionDTO wineVersionDTO) {
        this.setOnDeleteEngine.accept(wineVersionDTO);
    }

    public void showWizard(Tab tab) {
        showRightView(tab.getContent());
    }
}
