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
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.collections.ExpandedList;
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
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObservableList<ContainerCategoryDTO> categories;

    private final CombinedListWidget<ContainerDTO> availableContainers;

    private Consumer<ContainerDTO> onSelectContainer;

    /**
     * Constructor
     *
     * @param themeManager The theme manager
     * @param javaFxSettingsManager The javafx settings manager
     */
    public ContainersView(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Containers"), themeManager);

        this.javaFxSettingsManager = javaFxSettingsManager;

        this.categories = FXCollections.observableArrayList();

        this.filter = new ContainersFilter();

        filter.selectedContainerCategoryProperty().addListener((Observable invalidation) -> closeDetailsView());

        this.availableContainers = createCombinedListWidget();

        setSidebar(createContainersSidebar(availableContainers));
    }

    private ContainersSidebar createContainersSidebar(CombinedListWidget<ContainerDTO> availableContainers) {
        /*
         * initialize the container categories by sorting them
         */
        final SortedList<ContainerCategoryDTO> sortedCategories = categories
                .sorted(Comparator.comparing(ContainerCategoryDTO::getName));

        return new ContainersSidebar(filter, javaFxSettingsManager, sortedCategories, availableContainers);
    }

    private CombinedListWidget<ContainerDTO> createCombinedListWidget() {
        /*
         * initialize the container lists by:
         * 1. sorting the containers by their name
         * 2. filtering the containers
         */
        final FilteredList<ContainerDTO> filteredContainers = new ExpandedList<>(
                categories.sorted(Comparator.comparing(ContainerCategoryDTO::getName)),
                ContainerCategoryDTO::getContainers)
                        .sorted(Comparator.comparing(ContainerDTO::getName))
                        .filtered(filter::filter);

        filteredContainers.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter, filter.searchTermProperty()));

        return new CombinedListWidget<>(filteredContainers, ListWidgetEntry::create,
                (element, event) -> showContainerDetails(element));
    }

    /**
     * Populate with a list of container categories
     *
     * @param categories ContainerCategoryDTO
     */
    public void populate(List<ContainerCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            closeDetailsView();
            setCenter(availableContainers);
        });
    }

    /**
     * Displays the details view for a given container.
     *
     * @param container The container, whose details should be shown.
     */
    private void showContainerDetails(ContainerDTO container) {
        // TODO: separate details panel and controller
        onSelectContainer.accept(container);
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
