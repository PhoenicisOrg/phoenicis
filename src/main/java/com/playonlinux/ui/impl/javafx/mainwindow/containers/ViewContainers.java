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

import com.playonlinux.containers.entities.ContainerEntity;
import com.playonlinux.containers.entities.ContainersWindowEntity;
import com.playonlinux.containers.entities.WinePrefixContainerEntity;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.ui.impl.javafx.mainwindow.*;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import static com.playonlinux.core.lang.Localisation.translate;

public class ViewContainers extends MainWindowView implements Observer<Observable, ContainersWindowEntity> {
    private final EventHandlerContainers eventHandlerContainers;
    private final LeftButtonGroup containersView;

    private TextField searchBar;
    private MessagePanel selectContainerPanel;
    private final WeakHashMap<ContainerEntity, ContainerConfigurationView<?>> containerConfigurationViewsCache;

    public ViewContainers(MainWindow parent) {
        super(parent);

        eventHandlerContainers = new EventHandlerContainers();

        this.containersView = new LeftButtonGroup(translate("Containers"));

        this.drawSideBar();
        eventHandlerContainers.getContainers().addObserver(this);

        initSelectContainerPane();
        showRightView(selectContainerPanel);
        containerConfigurationViewsCache = new WeakHashMap<>();
    }

    private void initSelectContainerPane() {
        this.selectContainerPanel = new MessagePanel(translate("Please select a container to configure"));
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

        for(ContainerEntity containerEntity: argument.getContainerEntities()) {
            final LeftButton containerSelector = new LeftButton("containers/container.png", containerEntity.getName());
            leftButtonList.add(containerSelector);
            containerSelector.setOnMouseClicked(event -> selectContainer(containerEntity));
        }

        containersView.setButtons(leftButtonList);
    }

    private void selectContainer(ContainerEntity containerEntity) {
        if(containerConfigurationViewsCache.keySet().contains(containerEntity)) {
            this.showRightView(containerConfigurationViewsCache.get(containerEntity));
        } else {
            ContainerConfigurationView<?> containerConfigurationView = createContainerConfigurationView(containerEntity);
            containerConfigurationViewsCache.put(containerEntity, containerConfigurationView);
            this.showRightView(containerConfigurationView);
        }
    }

    private ContainerConfigurationView<?> createContainerConfigurationView(ContainerEntity containerEntity) {
        /* Not perfect. Needs more abstraction  */
        if(containerEntity instanceof WinePrefixContainerEntity) {
            return new WinePrefixContainerConfigurationView((WinePrefixContainerEntity) containerEntity, eventHandlerContainers);
        } else {
            return new GenericContainerConfigurationView(containerEntity);
        }
    }
}
