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

package com.playonlinux.ui.impl.javafx.mainwindow.containers;

import com.playonlinux.containers.Container;
import com.playonlinux.containers.entities.ContainerEntity;
import com.playonlinux.containers.entities.ContainersWindowEntity;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.ui.impl.javafx.mainwindow.*;

import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;


public class ViewContainers extends MainWindowView implements Observer<Observable, ContainersWindowEntity> {
    private final EventHandlerContainers eventHandlerContainers;
    private final LeftButtonGroup containersView;

    private TextField searchBar;

    public ViewContainers(MainWindow parent) {
        super(parent);

        eventHandlerContainers = new EventHandlerContainers();

        this.containersView = new LeftButtonGroup(translate("Containers"));

        this.drawSideBar();
        eventHandlerContainers.getContainers().addObserver(this);
    }



    protected void drawSideBar() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased((e) -> applyFilter(searchBar.getText()));

        addToSideBar(searchBar, new LeftSpacer(), containersView);

        super.drawSideBar();
    }


    private void applyFilter(String searchText) {
        this.eventHandlerContainers.getContainers().applyFilter(item -> item.getName().toLowerCase().contains(searchText.toLowerCase()));
    }

    @Override
    public void update(Observable observable, ContainersWindowEntity argument) {
        final List<LeftButton> leftButtonList = new ArrayList<>();

        for(ContainerEntity containersWindowEntity: argument.getContainerEntities()) {
            leftButtonList.add(new LeftButton("containers/container.png", containersWindowEntity.getName()));
        }

        containersView.setButtons(leftButtonList);
    }
}
