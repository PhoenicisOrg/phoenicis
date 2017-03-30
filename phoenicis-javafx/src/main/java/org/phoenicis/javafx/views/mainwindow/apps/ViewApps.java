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

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.apps.dto.ScriptDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.ui.*;
import org.phoenicis.settings.SettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewApps extends MainWindowView {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewApps.class);

    private final MiniatureListWidget<ApplicationDTO> availableApps;
    private final LeftToggleGroup<CategoryDTO> categoryView;
    private final ApplicationFilter<ApplicationDTO> filter;
    private final SearchBox searchBar;

    private Consumer<ScriptDTO> onSelectScript = (script) -> { };

    private ObservableList<CategoryDTO> categories;
    private FilteredList<CategoryDTO> installableCategories;
    private SortedList<CategoryDTO> sortedCategories;

    private ObservableList<ApplicationDTO> applications;
    private FilteredList<ApplicationDTO> filteredApplications;
    private SortedList<ApplicationDTO> sortedApplications;

    private PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

    public ViewApps(ThemeManager themeManager, SettingsManager settingsManager) {
        super("Apps", themeManager);

        this.searchBar = new SearchBox(themeManager, this::processFilterText, this::clearFilterText);

        this.availableApps = MiniatureListWidget.create(MiniatureListWidget.Element::create, (element, event) -> showAppDetails(element.getValue(), settingsManager));
        this.categoryView = LeftToggleGroup.create(translate("Categories"), this::createAllCategoriesToggleButton, this::createCategoryToggleButton);

        this.categories = FXCollections.observableArrayList();
        this.installableCategories = this.categories.filtered(category -> category.getType() == CategoryDTO.CategoryType.INSTALLERS);
        this.sortedCategories = this.installableCategories.sorted(Comparator.comparing(CategoryDTO::getName));

        this.applications = FXCollections.observableArrayList();
        this.filteredApplications = new FilteredList<ApplicationDTO>(this.applications);
        this.sortedApplications = this.filteredApplications.sorted(Comparator.comparing(ApplicationDTO::getName));

        this.filter = new ApplicationFilter<ApplicationDTO>(filteredApplications, (filterText, application) -> application.getName().toLowerCase().contains(filterText));

        this.installableCategories.addListener((ListChangeListener<? super CategoryDTO>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (CategoryDTO removedCategory : change.getRemoved()) {
                        this.applications.removeAll(removedCategory.getApplications());
                    }
                }

                if (change.wasAdded()) {
                    for (CategoryDTO addedCategory : change.getAddedSubList()) {
                        this.applications.addAll(addedCategory.getApplications());
                    }
                }
            }
        });

        Bindings.bindContent(this.categoryView.getElements(), this.sortedCategories);
        Bindings.bindContent(this.availableApps.getItems(), this.sortedApplications);

        this.drawSideBar();
        this.showWait();
    }

    public void setOnSelectScript(Consumer<ScriptDTO> onSelectScript) {
        this.onSelectScript = onSelectScript;
    }

    /**
     * Show available apps panel
     */
    public void showAvailableApps() {
        showRightView(availableApps);
    }

    /**
     * Populate with a list of categories containing apps, and then scripts
     *
     * @param categories CategoryDTO
     */
    public void populate(List<CategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);
            this.filter.clearAll();
            this.categoryView.selectAll();
            this.showAvailableApps();
        });
    }

    public void setOnRetryButtonClicked(EventHandler<? super MouseEvent> event) {
        getFailurePanel().getRetryButton().setOnMouseClicked(event);
    }

    @Override
    protected void drawSideBar() {
        final CheckBox testingCheck = new LeftCheckBox(translate("Testing"));
        final CheckBox noCdNeededCheck = new LeftCheckBox(translate("No CD needed"));
        final CheckBox commercialCheck = new LeftCheckBox(translate("Commercial"));

        addToSideBar(
                searchBar,
                new LeftSpacer(),
                categoryView,
                new LeftSpacer(),
                new LeftBarTitle("Filters"),
                testingCheck,
                noCdNeededCheck,
                commercialCheck
        );

        super.drawSideBar();
    }

    private void showAppDetails(ApplicationDTO application, SettingsManager settingsManager) {
        final AppPanel appPanel = new AppPanel(application, themeManager, settingsManager);
        appPanel.setOnScriptInstall(this::installScript);
        showRightView(appPanel);
    }

    private void installScript(ScriptDTO scriptDTO) {
        this.onSelectScript.accept(scriptDTO);
    }

    private void clearFilterText() {
        this.filter.setFilterText("");
        this.showAvailableApps();
    }

    private void processFilterText(String filterText) {
        this.pause.setOnFinished(event -> {
            String text = filterText.toLowerCase();

            if (text != null && text.length() >= 3) {
                filter.setFilterText(text);
            } else {
                filter.setFilterText("");
            }
        });

        this.pause.playFromStart();

        this.showAvailableApps();
    }

    private ToggleButton createAllCategoriesToggleButton() {
        final LeftToggleButton allCategoryButton = new LeftToggleButton("All");
        allCategoryButton.setSelected(true);
        final String allCategoryButtonIcon = String.format("icons/mainwindow/apps/all.png");
        allCategoryButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(allCategoryButtonIcon) + "');");
        allCategoryButton.setOnMouseClicked(event -> {
            filter.clearFilters();
            showAvailableApps();
        });

        return allCategoryButton;
    }

    private ToggleButton createCategoryToggleButton(CategoryDTO category) {
        final LeftToggleButton categoryButton = new LeftToggleButton(category.getName());

        final String resource = String.format("icons/mainwindow/apps/%s.png", category.getName().toLowerCase());
        if (themeManager.resourceExists(resource)) {
            categoryButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(resource) + "');");
        } else {
            categoryButton.setStyle("-fx-background-image: url('" + category.getIcon() + "');");
        }
        categoryButton.setOnMouseClicked(event -> {
            filter.setFilters(category.getApplications()::contains);
            showAvailableApps();
        });

        return categoryButton;
    }
}
