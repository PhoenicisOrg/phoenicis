/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.javafx.views.mainwindow.engines;

import com.playonlinux.engines.dto.WineVersionDTO;
import com.playonlinux.engines.dto.WineVersionDistributionDTO;
import com.playonlinux.javafx.views.common.widget.MiniatureListWidget;
import com.playonlinux.javafx.views.common.widget.StaticMiniature;
import com.playonlinux.javafx.views.mainwindow.FailurePanel;
import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import com.playonlinux.javafx.views.mainwindow.WaitPanel;
import com.playonlinux.javafx.views.mainwindow.ui.LeftBarTitle;
import com.playonlinux.javafx.views.mainwindow.ui.LeftButton;
import com.playonlinux.javafx.views.mainwindow.ui.LeftSpacer;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.List;

public class ViewEngines extends MainWindowView {
    private TabPane wineDistributionsTabPane;
    private FailurePanel failurePanel;
    private HBox waitPanel;

    public ViewEngines() {
        super();

        //   entitiesProvider = eventHandlerLibrary.getRemoteWineVersions();

        this.initWait();
        this.initFailure();
        this.initWineVersions();

        this.drawSideBar();
        this.showWait();
    }

    private void initFailure() {
        failurePanel = new FailurePanel();
    }

    private void initWait() {
        waitPanel = new WaitPanel();
    }

    private void initWineVersions() {
        wineDistributionsTabPane = new TabPane();
        wineDistributionsTabPane.getStyleClass().add("rightPane");

        wineDistributionsTabPane.prefWidthProperty().bind(this.widthProperty());
    }

    private void showWait() {
        showRightView(waitPanel);
    }

    private void showFailure() {
        showRightView(failurePanel);
    }

    public void showWineVersions() {
        showRightView(wineDistributionsTabPane);
    }

    @Override
    protected void drawSideBar() {
        super.drawSideBar();

        final TextField searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> {
        });

        LeftButton wine = new LeftButton("/com/playonlinux/javafx/views/mainwindow/engines/wine.png", "Wine");

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Engines"), wine);
    }

    public void setUpEvents() {
        //entitiesProvider.setOnChange(this::update);
    }


    public void populate(List<WineVersionDistributionDTO> wineVersionDistributionDTOs) {
        for (WineVersionDistributionDTO wineVersionDistributionDTO : wineVersionDistributionDTOs) {
            wineDistributionsTabPane.getTabs().add(createWineDistributionTab(wineVersionDistributionDTO));
        }
    }

    private Tab createWineDistributionTab(WineVersionDistributionDTO wineVersionDistributionDTO) {
        final MiniatureListWidget tabContent = MiniatureListWidget.create();
        List<WineVersionDTO> packages = wineVersionDistributionDTO.getPackages();
        packages.sort(WineVersionDistributionDTO.comparator().reversed());

        for (WineVersionDTO wineVersionDTO :
                packages) {
            tabContent.addItem(wineVersionDTO.getVersion(), new StaticMiniature(StaticMiniature.WINE_MINIATURE));
        }

        return new Tab(wineVersionDistributionDTO.getDescription(), tabContent);
    }
}
