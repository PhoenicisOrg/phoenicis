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

package com.playonlinux.javafx.mainwindow.engines;

import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.engines.wine.entities.WineVersionDistributionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionsWindowEntity;
import com.playonlinux.javafx.common.widget.MiniatureListWidget;
import com.playonlinux.javafx.common.widget.StaticMiniature;
import com.playonlinux.javafx.mainwindow.FailurePanel;
import com.playonlinux.javafx.mainwindow.LeftBarTitle;
import com.playonlinux.javafx.mainwindow.LeftButton;
import com.playonlinux.javafx.mainwindow.LeftSpacer;
import com.playonlinux.javafx.mainwindow.MainWindow;
import com.playonlinux.javafx.mainwindow.MainWindowView;
import com.playonlinux.javafx.mainwindow.WaitPanel;
import com.playonlinux.ui.api.EntitiesProvider;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ViewEngines extends MainWindowView implements Observer<Observable, WineVersionsWindowEntity> {
    private final EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> entitiesProvider;
    private TabPane wineDistributions;
    private FailurePanel failurePanel;
    private HBox waitPanel;

    public ViewEngines(MainWindow parent) {
        super(parent);

        final EventHandlerEngines eventHandlerLibrary = new EventHandlerEngines();
        entitiesProvider = eventHandlerLibrary.getRemoteWineVersions();

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
        wineDistributions = new TabPane();
        wineDistributions.getStyleClass().add("rightPane");
    }

    private void showWait() {
        showRightView(waitPanel);
    }

    private void showFailure() {
        showRightView(failurePanel);
    }

    private void showWineVersions() {
        showRightView(wineDistributions);
    }

    @Override
    protected void drawSideBar() {
        super.drawSideBar();

        final TextField searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> {
        });

        LeftButton wine = new LeftButton("/com/playonlinux/javafx/mainwindow/engines/wine.png", "Wine");

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Engines"), wine);
    }

    public void setUpEvents() {
        entitiesProvider.addObserver(this);
    }

    @Override
    public void update(Observable observable, WineVersionsWindowEntity argument) {
        Platform.runLater(() -> {
            if (argument.isDownloading()) {
                this.showWait();
            } else if (argument.isDownloadFailed()) {
                this.showFailure();
            } else {
                this.showWineVersions();
                wineDistributions.getTabs().clear();

                for (WineVersionDistributionItemEntity wineVersionDistributionItemEntity : argument
                        .getDistributions()) {
                    final MiniatureListWidget miniatureListWidget = MiniatureListWidget.create();
                    final Tab wineDistributionTab = new Tab();
                    wineDistributionTab.setClosable(false);
                    wineDistributionTab.setText(wineVersionDistributionItemEntity.getDescription());
                    wineDistributionTab.setContent(miniatureListWidget);

                    for (WineVersionItemEntity wineVersionItemEntity : wineVersionDistributionItemEntity
                            .getAvailablePackages()) {
                        miniatureListWidget.addItem(wineVersionItemEntity.getVersion(),
                                new StaticMiniature(StaticMiniature.WINE_MINIATURE));
                    }

                    wineDistributions.getTabs().add(wineDistributionTab);
                }
            }
        });
    }
}
