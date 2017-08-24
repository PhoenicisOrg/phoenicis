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
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ExpandedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.tools.ToolsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ViewApps extends MainWindowView<ApplicationSidebar> {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewApps.class);

    private final CombinedListWidget<ApplicationDTO> availableApps;
    private final ApplicationFilter filter;

    private Consumer<ScriptDTO> onSelectScript;

    private ObservableList<CategoryDTO> categories;
    private FilteredList<CategoryDTO> installableCategories;
    private SortedList<CategoryDTO> sortedCategories;

    private ObservableList<ApplicationDTO> applications;
    private FilteredList<ApplicationDTO> filteredApplications;
    private SortedList<ApplicationDTO> sortedApplications;

    public ViewApps(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager,
            ToolsConfiguration toolsConfiguration) {
        super(tr("Apps"), themeManager);

        this.availableApps = new CombinedListWidget<ApplicationDTO>(ListWidgetEntry::create,
                (element, event) -> showAppDetails(element, javaFxSettingsManager));

        this.filter = new ApplicationFilter(toolsConfiguration.operatingSystemFetcher(),
                (filterText, application) -> FuzzySearch.partialRatio(application.getName().toLowerCase(),
                        filterText) > javaFxSettingsManager.getFuzzySearchRatio());

        // initialising the category lists
        this.categories = FXCollections.observableArrayList();
        this.installableCategories = this.categories
                .filtered(category -> category.getType() == CategoryDTO.CategoryType.INSTALLERS);
        this.sortedCategories = this.installableCategories.sorted(Comparator.comparing(CategoryDTO::getName));

        // initialising the application lists
        this.applications = new ExpandedList<ApplicationDTO, CategoryDTO>(this.installableCategories,
                CategoryDTO::getApplications);
        this.filteredApplications = new FilteredList<ApplicationDTO>(this.applications);
        this.filteredApplications.predicateProperty().bind(filter.applicationFilterProperty());
        this.sortedApplications = this.filteredApplications.sorted(Comparator.comparing(ApplicationDTO::getName));

        this.sidebar = new ApplicationSidebar(availableApps, filter, javaFxSettingsManager);

        // create the bindings between the visual components and the observable lists
        this.sidebar.bindCategories(this.sortedCategories);
        this.availableApps.bind(this.sortedApplications);

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

    public void setOnSelectScript(Consumer<ScriptDTO> onSelectScript) {
        this.onSelectScript = onSelectScript;
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

    public void setOnRetryButtonClicked(EventHandler<? super MouseEvent> event) {
        getFailurePanel().getRetryButton().setOnMouseClicked(event);
    }

    private void showAppDetails(ApplicationDTO application, JavaFxSettingsManager javaFxSettingsManager) {
        final AppPanel appPanel = new AppPanel(application, filter, themeManager, javaFxSettingsManager);
        appPanel.setOnScriptInstall(this::installScript);
        appPanel.setOnClose(this::closeDetailsView);
        appPanel.setMaxWidth(400);
        this.showDetailsView(appPanel);
    }

    private void installScript(ScriptDTO scriptDTO) {
        this.onSelectScript.accept(scriptDTO);
    }
}
