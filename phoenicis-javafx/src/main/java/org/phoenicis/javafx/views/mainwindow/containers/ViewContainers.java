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

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.views.common.ExpandedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ViewContainers extends MainWindowView<ContainerSideBar> {
    private ContainerSideBar sideBar;

    private Consumer<ContainerDTO> onSelectContainer = (container) -> {
    };

    private final MiniatureListWidget<ContainerDTO> availableContainers;

    private ObservableList<ContainerCategoryDTO> categories;
    private SortedList<ContainerCategoryDTO> sortedCategories;

    private ObservableList<ContainerDTO> containers;
    private SortedList<ContainerDTO> sortedContainers;

    public ViewContainers(ThemeManager themeManager) {
        super("Containers", themeManager);

        this.sideBar = new ContainerSideBar();

        this.availableContainers = MiniatureListWidget.create(MiniatureListWidget.Element::create,
                (element, event) -> showContainerDetails(element.getValue()));

        this.categories = FXCollections.observableArrayList();
        this.sortedCategories = this.categories.sorted(Comparator.comparing(ContainerCategoryDTO::getName));

        this.containers = new ExpandedList<ContainerDTO, ContainerCategoryDTO>(this.sortedCategories,
                ContainerCategoryDTO::getContainers);
        this.sortedContainers = this.containers.sorted(Comparator.comparing(ContainerDTO::getName));

        this.sideBar.setOnApplyFilter(this::applyFilter);

        this.sideBar.bindCategories(this.sortedCategories);
        Bindings.bindContent(this.availableContainers.getItems(), this.sortedContainers);

        // set the category selection consumers
        this.sideBar.setOnCategorySelection(category -> showAvailableContainers());
        this.sideBar.setOnAllCategorySelection(this::showAvailableContainers);

        this.setSideBar(sideBar);
    }

    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.onSelectContainer = onSelectContainer;
    }

    /**
     * Show available containers panel
     */
    public void showAvailableContainers() {
        this.closeDetailsView();
        setCenter(availableContainers);
    }

    /**
     * Populate with a list of container categories
     *
     * @param categories ContainerCategoryDTO
     */
    public void populate(List<ContainerCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            this.sideBar.selectAllCategories();

            this.showAvailableContainers();
        });
    }

    private void showContainerDetails(ContainerDTO container) {
        // TODO: separate details panel and controller
        this.onSelectContainer.accept(container);
    }

    private void applyFilter(String searchText) {
        // TODO: Do some filtering here
    }
}
