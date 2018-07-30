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

package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.lists.ExpandedList;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.tools.ToolsConfiguration;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * This view contains the available applications list.
 * This component is made up of up to three components:
 * <p>
 * <ul>
 * <li>A sidebar containing the categories of the applications and tools to filter the applications</li>
 * <li>A list widget showing the available applications to the user</li>
 * <li>An optional details view that contains the information and available scripts for a selected application</li>
 * </ul>
 */
public class ApplicationsView extends MainWindowView<ApplicationsSidebar> {

    private final ApplicationFilter filter;
    private final CombinedListWidget<ApplicationDTO> availableApps;

    private Consumer<ScriptDTO> onSelectScript;

    private ObservableList<CategoryDTO> categories;
    private FilteredList<CategoryDTO> installableCategories;
    private SortedList<CategoryDTO> sortedCategories;

    private ObservableList<ApplicationDTO> applications;
    private SortedList<ApplicationDTO> sortedApplications;
    private PhoenicisFilteredList<ApplicationDTO> filteredApplications;

    /**
     * Constructor
     *
     * @param themeManager The theme manager
     * @param javaFxSettingsManager The javafx settings manager
     * @param toolsConfiguration The tools configuration
     */
    public ApplicationsView(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager,
            ToolsConfiguration toolsConfiguration) {
        super(tr("Apps"), themeManager);

        this.availableApps = new CombinedListWidget<ApplicationDTO>(ListWidgetEntry::create,
                (element, event) -> showAppDetails(element, javaFxSettingsManager));

        this.filter = new ApplicationFilter(toolsConfiguration.operatingSystemFetcher(),
                (filterText, application) -> {
                    if (StringUtils.isNotEmpty(filterText)) {
                        return FuzzySearch.partialRatio(application.getName().toLowerCase(),
                                filterText) > javaFxSettingsManager.getFuzzySearchRatio();
                    } else {
                        return true;
                    }
                });

        /*
         * initialize the category lists by:
         * 1. filtering by installer categories
         * 2. sorting the remaining categories by their name
         */
        this.categories = FXCollections.observableArrayList();
        this.installableCategories = this.categories
                .filtered(category -> category.getType() == CategoryDTO.CategoryType.INSTALLERS);
        this.sortedCategories = this.installableCategories.sorted(Comparator.comparing(CategoryDTO::getName));

        /*
         * initialising the application lists by:
         * 1. sorting the applications by their name
         * 2. filtering them
         */
        this.applications = new ExpandedList<>(this.installableCategories, CategoryDTO::getApplications);
        this.sortedApplications = this.applications.sorted(Comparator.comparing(ApplicationDTO::getName));
        this.filteredApplications = new PhoenicisFilteredList<>(this.sortedApplications, filter::filter);
        filter.addOnFilterChanged(filteredApplications::trigger);

        this.sidebar = new ApplicationsSidebar(availableApps, filter, javaFxSettingsManager);

        // create the bindings between the visual components and the observable lists
        this.sidebar.bindCategories(this.sortedCategories);
        this.availableApps.bind(this.filteredApplications);

        // set the category selection consumers
        this.sidebar.setOnCategorySelection(category -> {
            filter.setFilterCategory(category);
            this.closeDetailsView();
        });
        this.sidebar.setOnAllCategorySelection(() -> {
            filter.setFilterCategory(null);
            this.closeDetailsView();
        });

        this.setSidebar(this.sidebar);
    }

    /**
     * Populate with a list of categories containing apps, and then scripts
     *
     * @param categories CategoryDTO
     */
    public void populate(List<CategoryDTO> categories) {
        List<CategoryDTO> filteredCategories = categories.stream()
                .filter(category -> !category.getApplications().isEmpty()).collect(Collectors.toList());

        Platform.runLater(() -> {
            this.categories.setAll(filteredCategories);
            this.filter.clearAll();
            this.sidebar.selectAllCategories();

            this.closeDetailsView();
            this.setCenter(availableApps);
        });
    }

    /**
     * Sets the callback, which is called when a script has been selected
     *
     * @param onSelectScript The callback, which is called when a script has been selected
     */
    public void setOnSelectScript(Consumer<ScriptDTO> onSelectScript) {
        this.onSelectScript = onSelectScript;
    }

    public void setOnRetryButtonClicked(EventHandler<? super MouseEvent> event) {
        getFailurePanel().getRetryButton().setOnMouseClicked(event);
    }

    /**
     * Displays the application details view on the right side for a given application.
     *
     * @param application The application, whose details should be shown
     * @param javaFxSettingsManager The javafx settings manager
     */
    private void showAppDetails(ApplicationDTO application, JavaFxSettingsManager javaFxSettingsManager) {
        final ApplicationPanel applicationPanel = new ApplicationPanel(application, filter, themeManager,
                javaFxSettingsManager);
        applicationPanel.setOnScriptInstall(this::installScript);
        applicationPanel.setOnClose(this::closeDetailsView);
        applicationPanel.setMaxWidth(400);
        applicationPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));
        this.showDetailsView(applicationPanel);
    }

    /**
     * Starts the installation process for a given script
     *
     * @param scriptDTO The script to be installed
     */
    private void installScript(ScriptDTO scriptDTO) {
        this.onSelectScript.accept(scriptDTO);
    }
}
