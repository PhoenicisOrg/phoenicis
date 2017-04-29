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

package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.MessagePanel;
import org.phoenicis.javafx.views.mainwindow.ui.LeftGroup;
import org.phoenicis.javafx.views.mainwindow.ui.LeftSpacer;
import org.phoenicis.javafx.views.mainwindow.ui.LeftToggleButton;
import org.phoenicis.javafx.views.mainwindow.ui.SearchBox;

import javafx.application.Platform;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewContainers extends MainWindowView<ContainerSideBar> {
    private ContainerSideBar sideBar;

    private ObservableList<ContainerDTO> containers;

    private MessagePanel selectContainerPanel;

    public ViewContainers(ThemeManager themeManager) {
        super("Containers", themeManager);

        this.sideBar = new ContainerSideBar();

        this.containers = FXCollections.observableArrayList();

        this.sideBar.setOnApplyFilter(this::applyFilter);

        this.sideBar.bindContainers(this.containers);

        this.initSelectContainerPane();

        this.setSideBar(sideBar);
        this.showRightView(selectContainerPanel);
    }

    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.sideBar.setOnSelectContainer(onSelectContainer);
    }

    private void initSelectContainerPane() {
        this.selectContainerPanel = new MessagePanel(translate("Please select a container to configure"));
    }

    public void populate(List<ContainerDTO> containers) {
        Platform.runLater(() -> {
            this.containers.setAll(containers);

            this.initSelectContainerPane();

            showRightView(selectContainerPanel);
        });
    }

    private void applyFilter(String searchText) {
        // TODO: Do some filtering here
    }
}
