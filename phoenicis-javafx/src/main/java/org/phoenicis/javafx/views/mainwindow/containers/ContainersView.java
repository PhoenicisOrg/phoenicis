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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ExpandedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ContainersView extends MainWindowView<ContainersSidebar> {
    private Consumer<ContainerDTO> onSelectContainer;

    private final CombinedListWidget<ContainerDTO> availableContainers;

    private ObservableList<ContainerCategoryDTO> categories;
    private SortedList<ContainerCategoryDTO> sortedCategories;

    private ObservableList<ContainerDTO> containers;
    private SortedList<ContainerDTO> sortedContainers;

    public ContainersView(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Containers"), themeManager);

        this.availableContainers = new CombinedListWidget<ContainerDTO>(ListWidgetEntry::create,
                (element, event) -> showContainerDetails(element));
        this.sidebar = new ContainersSidebar(availableContainers, javaFxSettingsManager);

        this.categories = FXCollections.observableArrayList();
        this.sortedCategories = this.categories.sorted(Comparator.comparing(ContainerCategoryDTO::getName));

        this.containers = new ExpandedList<ContainerDTO, ContainerCategoryDTO>(this.sortedCategories,
                ContainerCategoryDTO::getContainers);
        this.sortedContainers = this.containers.sorted(Comparator.comparing(ContainerDTO::getName));

        this.sidebar.setOnApplyFilter(this::applyFilter);

        this.sidebar.bindCategories(this.sortedCategories);

        this.availableContainers.bind(sortedContainers);

        // set the category selection consumers
        this.sidebar.setOnCategorySelection(category -> closeDetailsView());
        this.sidebar.setOnAllCategorySelection(this::closeDetailsView);

        this.setSidebar(this.sidebar);
    }

    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.onSelectContainer = onSelectContainer;
    }

    /**
     * Populate with a list of container categories
     *
     * @param categories ContainerCategoryDTO
     */
    public void populate(List<ContainerCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            this.sidebar.selectAllCategories();

            this.closeDetailsView();
            this.setCenter(availableContainers);
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
