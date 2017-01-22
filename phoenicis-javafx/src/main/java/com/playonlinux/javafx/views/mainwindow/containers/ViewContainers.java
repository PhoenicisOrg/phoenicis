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

package com.playonlinux.javafx.views.mainwindow.containers;

import com.playonlinux.containers.dto.ContainerDTO;
import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import com.playonlinux.javafx.views.mainwindow.MessagePanel;
import com.playonlinux.javafx.views.mainwindow.ui.LeftGroup;
import com.playonlinux.javafx.views.mainwindow.ui.LeftSpacer;
import com.playonlinux.javafx.views.mainwindow.ui.LeftToggleButton;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ViewContainers extends MainWindowView {
    private final LeftGroup containersView;

    private TextField searchBar;
    private MessagePanel selectContainerPanel;
    private Consumer<ContainerDTO> onSelectContainer = container -> {};

    public ViewContainers() {
        super("Containers");

        this.containersView = new LeftGroup(translate("Containers"));

        this.drawSideBar();

        initSelectContainerPane();
        showRightView(selectContainerPanel);
    }

    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.onSelectContainer = onSelectContainer;
    }

    private void initSelectContainerPane() {
        this.selectContainerPanel = new MessagePanel(translate("Please select a container to configure"));
    }

    public void populate(List<ContainerDTO> containers) {
        Platform.runLater(() -> {
            final List<LeftToggleButton> leftButtonList = new ArrayList<>();
            ToggleGroup group = new ToggleGroup();

            for (ContainerDTO container : containers) {
                final LeftToggleButton containerButton = new LeftToggleButton("/com/playonlinux/javafx/views/mainwindow/containers/container.png", container.getName());
                containerButton.setToggleGroup(group);
                leftButtonList.add(containerButton);
                containerButton.setOnMouseClicked(event -> this.selectContainer(container));
            }

            initSelectContainerPane();
            showRightView(selectContainerPanel);
            containersView.setNodes(leftButtonList);
        });
    }

    private void selectContainer(ContainerDTO container) {
        this.onSelectContainer.accept(container);
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
