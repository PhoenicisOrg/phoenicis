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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widget.MiniatureListWidget;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.ui.*;
import org.phoenicis.repository.AppsSearchFilter;
import org.phoenicis.repository.CombinedAppsFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewApps extends MainWindowView {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewApps.class);
    private final MiniatureListWidget availableApps;
    private TextField searchBar;
    private LeftGroup categoryView;
    private Consumer<CategoryDTO> onSelectCategory = (category) -> {};
    private Consumer<ScriptDTO> onSelectScript = (script) -> {};
    private final CombinedAppsFilter currentFilter = new CombinedAppsFilter();
    private Consumer<CombinedAppsFilter> onApplyFilter = (filter) -> {};

    public ViewApps(ThemeManager themeManager) {
        super("Apps", themeManager);

        availableApps = MiniatureListWidget.create();

        this.drawSideBar();
        this.showWait();
    }

    public void setOnApplyFilter(Consumer<CombinedAppsFilter> onApplyFilter) {
        this.onApplyFilter = onApplyFilter;
    }

    public void setOnSelectCategory(Consumer<CategoryDTO> onSelectCategory) {
        this.onSelectCategory = onSelectCategory;
    }

    public void setOnSelectScript(Consumer<ScriptDTO> onSelectScript) {
        this.onSelectScript = onSelectScript;
    }

    /**
     * Show available repository panel
     */
    public void showAvailableApps() {
        showRightView(availableApps);
    }

    /**
     * Populate with a list of categories containing repository, and then scripts
     *
     * @param repositories RepositoryDTO
     */
    public void populate(List<RepositoryDTO> repositories) {
        Platform.runLater(() -> {
            final Optional<RepositoryDTO> repositoryDTO = repositories.stream().filter(repository -> repository.getType() == RepositoryDTO.RepositoryType.APPLICATIONS).findFirst();
            List<CategoryDTO> categories = repositoryDTO.isPresent() ? repositoryDTO.get().getCategories() : Collections.emptyList();

            final List<LeftButton> leftButtonList = new ArrayList<>();
            for (CategoryDTO category : categories) {
                final LeftButton categoryButton = new LeftButton(category.getName());
                final String resource = String.format("icons/mainwindow/repository/%s.png", category.getName().toLowerCase());
                if (themeManager.resourceExists(resource)) {
                    categoryButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(resource) + "');");
                } else {
                    categoryButton.setStyle("-fx-background-image: url('" + category.getIcon() + "');");
                }
                categoryButton.setOnMouseClicked(event -> selectCategory(category));
                leftButtonList.add(categoryButton);
            }

            categoryView.setNodes(leftButtonList);
            showAvailableApps();
        });
    }

    public void populateApps(List<ApplicationDTO> applications) {
        availableApps.clear();

        for (ApplicationDTO application : applications) {
            final Node applicationItem;
            if(application.getMiniatures().isEmpty()) {
                applicationItem = availableApps.addItem(application.getName());
            } else {
                applicationItem = availableApps.addItem(application.getName(), application.getMiniatures().get(0));
            }

            applicationItem.setOnMouseClicked(event -> this.showAppDetails(application));

        }
    }

    public void setOnRetryButtonClicked(EventHandler<? super MouseEvent> event) {
        getFailurePanel().getRetryButton().setOnMouseClicked(event);
    }

    @Override
    protected void drawSideBar() {
        searchBar = new TextField();
        searchBar.getStyleClass().add("searchBar");
        searchBar.textProperty().addListener(obs->{
            String filter = searchBar.getText().toLowerCase();
            currentFilter.clear();
            if(filter != null && filter.length() >= 3) {
                currentFilter.add(new AppsSearchFilter(filter));
            }
            onApplyFilter.accept(currentFilter);
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

    private void showAppDetails(ApplicationDTO application) {
        final AppPanel appPanel = new AppPanel(application, themeManager);
        appPanel.setOnScriptInstall(this::installScript);
        showRightView(appPanel);
    }

    private void installScript(ScriptDTO scriptDTO) {
        this.onSelectScript.accept(scriptDTO);
    }

    private void selectCategory(CategoryDTO category) {
        showRightView(availableApps);
        this.onSelectCategory.accept(category);
    }


}
