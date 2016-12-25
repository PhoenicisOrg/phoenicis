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

package com.playonlinux.javafx.views.mainwindow.containers;

import com.playonlinux.containers.dto.ContainerDTO;
import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import com.playonlinux.javafx.views.mainwindow.MessagePanel;
import com.playonlinux.javafx.views.mainwindow.ui.LeftButton;
import com.playonlinux.javafx.views.mainwindow.ui.LeftButtonGroup;
import com.playonlinux.javafx.views.mainwindow.ui.LeftSpacer;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ViewContainers extends MainWindowView {
    private final LeftButtonGroup containersView;

    private TextField searchBar;
    private MessagePanel selectContainerPanel;

    public ViewContainers() {
        super();

        this.containersView = new LeftButtonGroup(translate("Containers"));

        this.drawSideBar();

        initSelectContainerPane();
        showRightView(selectContainerPanel);
    }

    private void initSelectContainerPane() {
        this.selectContainerPanel = new MessagePanel(translate("Please select a container to configure"));
    }

    public void populate(List<ContainerDTO> containers) {
        final List<LeftButton> leftButtonList = new ArrayList<>();

        for(ContainerDTO container: containers) {
            final LeftButton containerSelector = new LeftButton("/com/playonlinux/javafx/views/mainwindow/containers/container.png", container.getName());
            leftButtonList.add(containerSelector);
            containerSelector.setOnMouseClicked(event -> this.selectContainer(container));
        }

        containersView.setButtons(leftButtonList);
    }

    private void selectContainer(ContainerDTO containerSelector) {

    }

    @Override
    protected void drawSideBar() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(e -> applyFilter(searchBar.getText()));

        addToSideBar(searchBar, new LeftSpacer(), containersView);

        super.drawSideBar();
    }

    private void applyFilter(String searchText) {

    }



}
