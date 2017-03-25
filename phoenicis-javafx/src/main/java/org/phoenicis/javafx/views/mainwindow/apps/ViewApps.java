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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.phoenicis.apps.AppsSearchFilter;
import org.phoenicis.apps.CombinedAppsFilter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewApps extends MainWindowView {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewApps.class);
    private final MiniatureListWidget availableApps;
    private SearchBox searchBar;
    private LeftGroup categoryView;
    private Consumer<List<CategoryDTO>> onSelectAll = (categories) -> {};
    private Consumer<CategoryDTO> onSelectCategory = (category) -> {};
    private Consumer<ScriptDTO> onSelectScript = (script) -> {};
    private final CombinedAppsFilter currentFilter = new CombinedAppsFilter();
    private Consumer<CombinedAppsFilter> onApplyFilter = (filter) -> {};
    private Consumer<List<CategoryDTO>> onSetDefaultCategoryIcons;

    public ViewApps(ThemeManager themeManager) {
        super("Apps", themeManager);

        availableApps = MiniatureListWidget.create();

        this.drawSideBar();
        this.showWait();
    }

    public void setOnApplyFilter(Consumer<CombinedAppsFilter> onApplyFilter) {
        this.onApplyFilter = onApplyFilter;
    }

    public void setOnSelectAll(Consumer<List<CategoryDTO>> onSelectAll) {
        this.onSelectAll = onSelectAll;
    }

    public void setOnSelectCategory(Consumer<CategoryDTO> onSelectCategory) {
        this.onSelectCategory = onSelectCategory;
    }

    public void setOnSetDefaultCategoryIcons(Consumer<List<CategoryDTO>> onSetDefaultCategoryIcons) {
        this.onSetDefaultCategoryIcons = onSetDefaultCategoryIcons;
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
            setDefaultCategoryIcons(categories);

            final List<LeftToggleButton> leftButtonList = new ArrayList<>();
            ToggleGroup group = new ToggleGroup();

            final LeftToggleButton allCategoryButton = new LeftToggleButton("All");
            allCategoryButton.setToggleGroup(group);
            allCategoryButton.setSelected(true);
            final String allCategoryButtonIcon = String.format("icons/mainwindow/apps/all.png");
            allCategoryButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(allCategoryButtonIcon) + "');");
            allCategoryButton.setOnMouseClicked(event -> selectAll(categories));
            leftButtonList.add(allCategoryButton);

            for (CategoryDTO category : categories) {
                if(category.getType() == CategoryDTO.CategoryType.INSTALLERS) {
                    final LeftToggleButton categoryButton = new LeftToggleButton(category.getName());
                    categoryButton.setToggleGroup(group);
                    final String themeName = new String(category.getName().toLowerCase() + "Button");
                    categoryButton.setId(themeName);
                    categoryButton.setOnMouseClicked(event -> selectCategory(category));
                    leftButtonList.add(categoryButton);
                }
            }

            categoryView.setNodes(leftButtonList);
            selectAll(categories);
        });
    }

    public void populateApps(List<ApplicationDTO> applications, SettingsManager settingsManager) {
        availableApps.clear();

        for (ApplicationDTO application : applications) {
            final Node applicationItem;
            if(application.getMiniatures().isEmpty()) {
                applicationItem = availableApps.addItem(application.getName());
            } else {
                applicationItem = availableApps.addItem(application.getName(), application.getMiniatures().get(0));
            }

            applicationItem.setOnMouseClicked(event -> this.showAppDetails(application, settingsManager));

        }
    }

    public void setOnRetryButtonClicked(EventHandler<? super MouseEvent> event) {
        getFailurePanel().getRetryButton().setOnMouseClicked(event);
    }

    @Override
    protected void drawSideBar() {
        searchBar = new SearchBox(themeManager);

        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

        searchBar.textProperty().addListener(obs-> {
            pause.setOnFinished(event -> {
                String filter = searchBar.getText().toLowerCase();
                currentFilter.clear();
                if (filter != null && filter.length() >= 3) {
                    currentFilter.add(new AppsSearchFilter(filter));
                }
                onApplyFilter.accept(currentFilter);
            });
            pause.playFromStart();
        });


        categoryView = new LeftGroup(translate("Categories"));

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

    private void setDefaultCategoryIcons(List<CategoryDTO> categories) {
        this.onSetDefaultCategoryIcons.accept(categories);
    }

    private void installScript(ScriptDTO scriptDTO) {
        this.onSelectScript.accept(scriptDTO);
    }

    private void selectAll(List<CategoryDTO> categories) {
        showRightView(availableApps);
        this.onSelectAll.accept(categories);
    }

    private void selectCategory(CategoryDTO category) {
        showRightView(availableApps);
        this.onSelectCategory.accept(category);
    }


}
