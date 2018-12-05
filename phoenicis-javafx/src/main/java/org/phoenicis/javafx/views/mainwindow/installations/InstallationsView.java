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

package org.phoenicis.javafx.views.mainwindow.installations;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.lists.ExpandedList;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The "Installations" tab shows the currently active installations.
 *
 * This includes applications as well as engines.
 */
public class InstallationsView extends MainWindowView<InstallationsSidebar> {
    private final InstallationsFilter filter;

    private InstallationsPanel installationsPanel;

    private CombinedListWidget<InstallationDTO> activeInstallations;

    private ObservableList<InstallationCategoryDTO> categories;
    private SortedList<InstallationCategoryDTO> sortedCategories;

    private ObservableList<InstallationDTO> installations;
    private PhoenicisFilteredList<InstallationDTO> filteredInstallations;
    private SortedList<InstallationDTO> sortedInstallations;

    private Runnable onInstallationAdded = () -> {
    };

    private Consumer<InstallationDTO> onInstallationSelected = installation -> {
    };

    /**
     * constructor
     * @param themeManager
     * @param javaFxSettingsManager
     */
    public InstallationsView(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Installations"), themeManager);
        this.getStyleClass().add("mainWindowScene");

        this.activeInstallations = new CombinedListWidget<>(ListWidgetEntry::create, (selectedItem, event) -> {

            this.activeInstallations.deselectAll();
            this.activeInstallations.select(selectedItem);
            this.onInstallationSelected.accept(selectedItem);
            showInstallationDetails(selectedItem);

            event.consume();
        });

        // initialize the category lists
        this.categories = FXCollections.observableArrayList();
        this.sortedCategories = this.categories.sorted(Comparator.comparing(InstallationCategoryDTO::getName));

        this.filter = new InstallationsFilter();

        // initialising the installations lists
        this.installations = new ExpandedList<>(this.sortedCategories, InstallationCategoryDTO::getInstallations);
        this.filteredInstallations = new PhoenicisFilteredList<>(this.installations, filter::filter);
        this.filter.addOnFilterChanged(filteredInstallations::trigger);

        this.sortedInstallations = this.filteredInstallations.sorted(Comparator.comparing(InstallationDTO::getName));

        this.activeInstallations.setOnMouseClicked(event -> {
            this.activeInstallations.deselectAll();
            this.onInstallationSelected.accept(null);
            event.consume();
        });

        this.sidebar = new InstallationsSidebar(activeInstallations, filter, javaFxSettingsManager);
        this.sidebar.bindCategories(this.sortedCategories);

        // set the category selection consumers
        this.sidebar.setOnCategorySelection(category -> {
            filter.setSelectedInstallationCategory(category);
            this.closeDetailsView();
        });
        this.sidebar.setOnAllCategorySelection(() -> {
            filter.setSelectedInstallationCategory(null);
            this.closeDetailsView();
        });

        this.setSidebar(this.sidebar);

        this.activeInstallations.bind(this.sortedInstallations);

        this.setCenter(this.activeInstallations);

        this.installationsPanel = new InstallationsPanel();
    }

    /**
     * shows the given installations
     * @param categories
     */
    private void populate(List<InstallationCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);
            this.filter.clear();

            this.closeDetailsView();
            this.setCenter(this.activeInstallations);
        });
    }

    /**
     * shows details of the given installation
     * @param installationDTO
     */
    private void showInstallationDetails(InstallationDTO installationDTO) {
        this.installationsPanel.setOnClose(this::closeDetailsView);
        this.installationsPanel.setInstallationDTO(installationDTO);
        this.installationsPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(2.5));
        this.showDetailsView(this.installationsPanel);
    }

    /**
     * adds new installation
     * @param installationDTO new installation
     */
    public void addInstallation(InstallationDTO installationDTO) {
        populate(new InstallationsUtils().addInstallationToList(this.categories, installationDTO));
        Platform.runLater(() -> {
            this.activeInstallations.deselectAll();
            this.activeInstallations.select(installationDTO);
            this.showInstallationDetails(installationDTO);
        });
        this.onInstallationAdded.run();
    }

    /**
     * removes installation (if it exists)
     * @param installationDTO installation to be removed
     */
    public void removeInstallation(InstallationDTO installationDTO) {
        populate(new InstallationsUtils().removeInstallationFromList(this.categories, installationDTO));
    }

    /**
     * sets Runnable which is executed whenever a new installation is added
     * @param onInstallationAdded
     */
    public void setOnInstallationAdded(Runnable onInstallationAdded) {
        this.onInstallationAdded = onInstallationAdded;
    }

}
