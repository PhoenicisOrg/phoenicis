package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.engines.CombinedEnginesFilter;
import org.phoenicis.engines.EnginesFilter;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * Created by marc on 22.04.17.
 */
public class EngineSideBar extends VBox {
    private SearchBox searchBar;

    private LeftToggleGroup<EngineCategoryDTO> categoryView;

    private LeftGroup installationFilterGroup;

    private CheckBox installedCheck;
    private CheckBox notInstalledCheck;

    private CombinedEnginesFilter currentFilter;

    private Consumer<CombinedEnginesFilter> onApplyFilter = (filter) -> {};

    private Consumer<EngineCategoryDTO> onCategorySelection;
    private Runnable onAllCategorySelection;

    public EngineSideBar() {
        this.initializeFilter();

        this.populateSearchBar();
        this.populateEngineTypes();
        this.populateInstallationFilters();

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.categoryView, new LeftSpacer(), this.installationFilterGroup);
    }

    public void bindEngineCategories(ObservableList<EngineCategoryDTO> engineCategories) {
        Bindings.bindContent(categoryView.getElements(), engineCategories);
    }

    private void initializeFilter() {
        this.currentFilter = new CombinedEnginesFilter();

        this.currentFilter.add(EnginesFilter.INSTALLED);
        this.currentFilter.add(EnginesFilter.NOT_INSTALLED);
    }

    private void populateSearchBar() {
        // TODO: do something when a search term has been entered
        this.searchBar = new SearchBox(filterText -> {}, () -> {});
    }

    private void populateEngineTypes() {
        this.categoryView = LeftToggleGroup.create(translate("Engines"), this::createCategoryToggleButton);
    }

    private void populateInstallationFilters() {
        this.installedCheck = new LeftCheckBox(translate("Installed"));
        this.installedCheck.setUserData(EnginesFilter.INSTALLED);
        this.installedCheck.setSelected(true);
        this.installedCheck.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            BooleanProperty selectedProperty = (BooleanProperty) observableValue;
            CheckBox checkBox = (CheckBox) selectedProperty.getBean();
            if (newValue) {
                currentFilter.add((EnginesFilter) checkBox.getUserData());
            } else {
                currentFilter.remove((EnginesFilter) checkBox.getUserData());
            }
            onApplyFilter.accept(currentFilter);
        });

        this.notInstalledCheck = new LeftCheckBox(translate("Not installed"));
        this.notInstalledCheck.setUserData(EnginesFilter.NOT_INSTALLED);
        this.notInstalledCheck.setSelected(true);
        this.notInstalledCheck.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            BooleanProperty selectedProperty = (BooleanProperty) observableValue;
            CheckBox checkBox = (CheckBox) selectedProperty.getBean();
            if (newValue) {
                currentFilter.add((EnginesFilter) checkBox.getUserData());
            } else {
                currentFilter.remove((EnginesFilter) checkBox.getUserData());
            }
            onApplyFilter.accept(currentFilter);
        });

        this.installationFilterGroup = new LeftGroup(installedCheck, notInstalledCheck);
    }

    private ToggleButton createCategoryToggleButton(EngineCategoryDTO category) {
        ToggleButton categoryButton = new LeftToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getName().toLowerCase()));
        categoryButton.setOnMouseClicked(event -> onCategorySelection.accept(category));

        return categoryButton;
    }

    public void setOnApplyFilter(Consumer<CombinedEnginesFilter> onApplyFilter) {
        this.onApplyFilter = onApplyFilter;
    }

    public void setOnCategorySelection(Consumer<EngineCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }

    public void setOnAllCategorySelection(Runnable onAllCategorySelection) {
        this.onAllCategorySelection = onAllCategorySelection;
    }
}
