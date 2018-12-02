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
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.lists.ExpandedList;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The containers view is responsible for showing all installed containers.
 * This view is partitioned in three sections:
 *
 * <ul>
 * <li>A sidebar, that allows the user to filter between the container categories</li>
 * <li>A list widget, which contains the installed containers</li>
 * <li>An optional details view showing details about the selected container in the list widget</li>
 * </ul>
 */
public class ContainersView extends MainWindowView<ContainersSidebar> {
    private final ContainersFilter filter;

    private final CombinedListWidget<ContainerDTO> availableContainers;

    private ObservableList<ContainerCategoryDTO> categories;
    private SortedList<ContainerCategoryDTO> sortedCategories;

    private ObservableList<ContainerDTO> containers;
    private SortedList<ContainerDTO> sortedContainers;
    private PhoenicisFilteredList<ContainerDTO> filteredContainers;

    private Consumer<ContainerDTO> onSelectContainer;

    /**
     * Constructor
     *
     * @param themeManager The theme manager
     * @param javaFxSettingsManager The javafx settings manager
     */
    public ContainersView(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Containers"), themeManager);

        this.filter = new ContainersFilter();

        this.availableContainers = new CombinedListWidget<ContainerDTO>(ListWidgetEntry::create,
                (element, event) -> showContainerDetails(element));
        this.sidebar = new ContainersSidebar(availableContainers, filter, javaFxSettingsManager);

        /*
         * initialize the container categories by sorting them
         */
        this.categories = FXCollections.observableArrayList();
        this.sortedCategories = this.categories.sorted(Comparator.comparing(ContainerCategoryDTO::getName));

        /*
         * initialize the container lists by:
         * 1. sorting the containers by their name
         * 2. filtering the containers
         */
        this.containers = new ExpandedList<>(this.sortedCategories, ContainerCategoryDTO::getContainers);
        this.sortedContainers = this.containers.sorted(Comparator.comparing(ContainerDTO::getName));
        this.filteredContainers = new PhoenicisFilteredList<>(this.sortedContainers, filter::filter);
        this.filter.addOnFilterChanged(filteredContainers::trigger);

        this.sidebar.bindCategories(this.sortedCategories);

        this.availableContainers.bind(this.filteredContainers);

        // set the category selection consumers
        this.sidebar.setOnCategorySelection(category -> closeDetailsView());
        this.sidebar.setOnAllCategorySelection(this::closeDetailsView);

        this.setSidebar(this.sidebar);
    }

    /**
     * Populate with a list of container categories
     *
     * @param categories ContainerCategoryDTO
     */
    public void populate(List<ContainerCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            this.closeDetailsView();
            this.setCenter(availableContainers);
        });
    }

    /**
     * Displays the details view for a given container.
     *
     * @param container The container, whose details should be shown.
     */
    private void showContainerDetails(ContainerDTO container) {
        // TODO: separate details panel and controller
        this.onSelectContainer.accept(container);
    }

    /**
     * Sets the callback, which is called when a container has been selected
     *
     * @param onSelectContainer The callback to be called when a container has been selected
     */
    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.onSelectContainer = onSelectContainer;
    }
}
