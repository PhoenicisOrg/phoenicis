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

import javafx.scene.Node;
import javafx.scene.control.*;
import org.phoenicis.engines.dto.EnginesFilter;
import javafx.scene.effect.ColorAdjust;
import org.phoenicis.engines.dto.WineVersionDTO;
import org.phoenicis.engines.dto.WineVersionDistributionDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.common.widget.StaticMiniature;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.ui.LeftBarTitle;
import org.phoenicis.javafx.views.mainwindow.ui.LeftButton;
import org.phoenicis.javafx.views.mainwindow.ui.LeftSpacer;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewEngines extends MainWindowView {
    private TabPane wineDistributionsTabPane;
    private Consumer<EnginesFilter> onApplyFilter = (filter) -> {};
    private Consumer<WineVersionDTO> setOnInstallEngine = (engine) -> {};
    private Consumer<WineVersionDTO> setOnDeleteEngine = (engine) -> {};

    public ViewEngines(ThemeManager themeManager) {
        super("Engines", themeManager);

        //   entitiesProvider = eventHandlerLibrary.getRemoteWineVersions();

        this.initFailure();
        this.initWineVersions();

        this.drawSideBar();
        this.showWait();
    }

    public void setOnApplyFilter(Consumer<EnginesFilter> onApplyFilter) {
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

        final ToggleGroup group = new ToggleGroup();
        final RadioButton allRadio = new RadioButton(translate("All"));
        allRadio.setUserData(EnginesFilter.ALL);
        allRadio.setToggleGroup(group);
        allRadio.setSelected(true);
        final RadioButton installedRadio = new RadioButton(translate("Installed"));
        installedRadio.setUserData(EnginesFilter.INSTALLED);
        installedRadio.setToggleGroup(group);
        final RadioButton notInstalledRadio = new RadioButton(translate("Not installed"));
        notInstalledRadio.setUserData(EnginesFilter.NOT_INSTALLED);
        notInstalledRadio.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (group.getSelectedToggle() != null) {
                onApplyFilter.accept((EnginesFilter) group.getSelectedToggle().getUserData());
            }
        });

        addToSideBar(searchBar, spacer, new LeftBarTitle("Engines"), wine, new LeftSpacer(), allRadio, installedRadio, notInstalledRadio);
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
}
