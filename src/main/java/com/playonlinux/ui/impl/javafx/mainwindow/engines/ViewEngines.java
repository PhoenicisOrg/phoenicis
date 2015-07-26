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

package com.playonlinux.ui.impl.javafx.mainwindow.engines;

import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.engines.wine.entities.WineVersionDistributionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionItemEntity;
import com.playonlinux.engines.wine.entities.WineVersionsWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.ui.impl.javafx.mainwindow.*;
import com.playonlinux.ui.impl.javafx.widget.MiniatureListWidget;
import com.playonlinux.ui.impl.javafx.widget.StaticMiniature;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class ViewEngines extends MainWindowView implements Observer<Observable, WineVersionsWindowEntity> {
    private final EntitiesProvider<WineVersionDistributionItemEntity, WineVersionsWindowEntity> entitiesProvider;
    private TabPane wineDistributions;

    public ViewEngines(MainWindow parent) {
        super(parent);

        EventHandlerEngines eventHandlerLibrary = new EventHandlerEngines();
        entitiesProvider = eventHandlerLibrary.getRemoteWineVersions();
        this.drawSideBar();
        this.drawWineVersions();

        showRightView(wineDistributions);
    }

    private void drawWineVersions() {
        wineDistributions = new TabPane();
        wineDistributions.getStyleClass().add("rightPane");
    }

    protected void drawSideBar() {
        super.drawSideBar();

        TextField searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> {
        });

        LeftButton wine = new LeftButton("/com/playonlinux/ui/impl/javafx/mainwindow/engines/wine.png", "Wine");

        wine.getStyleClass().add("leftPaneButtons");

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Engines"), wine);
    }

    public void setUpEvents() {
        entitiesProvider.addObserver(this);
    }

    @Override
    public void update(Observable observable, WineVersionsWindowEntity argument) {
        wineDistributions.getTabs().clear();


        for(WineVersionDistributionItemEntity wineVersionDistributionItemEntity : argument.getDistributions()) {
            final MiniatureListWidget miniatureListWidget = MiniatureListWidget.create();
            final Tab wineDistributionTab = new Tab();
            wineDistributionTab.setClosable(false);
            wineDistributionTab.setText(wineVersionDistributionItemEntity.getDescription());
            wineDistributionTab.setContent(miniatureListWidget);

            for(WineVersionItemEntity wineVersionItemEntity : wineVersionDistributionItemEntity.getAvailablePackages()) {
                miniatureListWidget.addItem(wineVersionItemEntity.getVersion(), new StaticMiniature(StaticMiniature.WINE_MINIATURE));
            }

            wineDistributions.getTabs().add(wineDistributionTab);
        }
    }
}
