package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the left sidebar of the engines tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an engine.
 * </li>
 * <li>
 * A button group containing a button for all known engine groups.
 * After pressing on one such button all engines belonging to the selected engine group are shown in the main window panel.
 * </li>
 * <li>
 * A button group containing buttons to filter for installed and uninstalled engines.
 * </li>
 * </ul>
 *
 * @author marc
 * @since 22.04.17
 */
public class EngineSidebar extends LeftSidebar {
    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private LeftScrollPane centerContent;

    // the button group containing a button for all engine categories
    private LeftToggleGroup<EngineCategoryDTO> categoryView;

    // the button group containing a button to filter the engines for installed and uninstalled engines
    private LeftGroup installationFilterGroup;

    private CheckBox installedCheck;
    private CheckBox notInstalledCheck;

    // widget to switch between the different list widgets in the center view
    private LeftListWidgetChooser<EngineVersionDTO> listWidgetChooser;

    // consumers called when an action inside the search bar has been performed
    private Consumer<String> onApplySearchTerm;
    private Runnable onSearchTermClear;

    // consumers called when a filter in the installation filter group has been activated
    private Consumer<Boolean> onApplyInstalledFilter;
    private Consumer<Boolean> onApplyUninstalledFilter;

    // consumer called when a category has been selected
    private Consumer<EngineCategoryDTO> onCategorySelection;

    private final JavaFxSettingsManager javaFxSettingsManager;

    /**
     * Constructor
     *
     * @param enginesVersionListWidgets The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public EngineSidebar(List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateEngineCategories();
        this.populateInstallationFilters();
        this.populateListWidgetChooser(enginesVersionListWidgets);

        this.centerContent = new LeftScrollPane(this.categoryView, new LeftSpacer(), this.installationFilterGroup);

        this.setTop(searchBar);
        this.setCenter(centerContent);
        this.setBottom(listWidgetChooser);
    }

    /**
     * This method takes an {@link ObservableList} of engine categories and binds it to the engine categories button group
     *
     * @param engineCategories The list of engine categories
     */
    public void bindEngineCategories(ObservableList<EngineCategoryDTO> engineCategories) {
        Bindings.bindContent(categoryView.getElements(), engineCategories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(filterText -> onApplySearchTerm.accept(filterText),
                () -> onSearchTermClear.run());
    }

    /**
     * This method populates the button group showing all known engine categories
     */
    private void populateEngineCategories() {
        this.categoryView = LeftToggleGroup.create(tr("Engines"), this::createCategoryToggleButton);
    }

    /**
     * This method populates the button group containing buttons to filter for installed and not installed engines
     */
    private void populateInstallationFilters() {
        this.installedCheck = new LeftCheckBox(tr("Installed"));
        this.installedCheck.setSelected(true);
        this.installedCheck.selectedProperty()
                .addListener((observableValue, oldValue, newValue) -> onApplyInstalledFilter.accept(newValue));

        this.notInstalledCheck = new LeftCheckBox(tr("Not installed"));
        this.notInstalledCheck.setSelected(true);
        this.notInstalledCheck.selectedProperty()
                .addListener((observableValue, oldValue, newValue) -> onApplyUninstalledFilter.accept(newValue));

        this.installationFilterGroup = new LeftGroup(installedCheck, notInstalledCheck);
    }

    /**
     * This method populates the list widget choose
     *
     * @param enginesVersionListWidgets The managed CombinedListWidgets
     */
    private void populateListWidgetChooser(List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets) {
        this.listWidgetChooser = new LeftListWidgetChooser<>(enginesVersionListWidgets);
        this.listWidgetChooser.setAlignment(Pos.BOTTOM_LEFT);
        this.listWidgetChooser.choose(this.javaFxSettingsManager.getEnginesListType());
        this.listWidgetChooser.setOnChoose(type -> {
            this.javaFxSettingsManager.setEnginesListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    /**
     * This method creates a new toggle button for a given engine category.
     *
     * @param category The engine category, for which a new toggle button should be created
     * @return The created toggle button
     */
    private ToggleButton createCategoryToggleButton(EngineCategoryDTO category) {
        ToggleButton categoryButton = new LeftToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getName().toLowerCase()));
        categoryButton.setOnAction(event -> onCategorySelection.accept(category));

        return categoryButton;
    }

    /**
     * This method selects the button belonging to the first engine category in the engine category button group.
     * If no engine category exists, this method will throw an {@link IllegalArgumentException}.
     *
     * @throws IllegalArgumentException
     */
    public void selectFirstEngineCategory() {
        this.categoryView.select(0);
    }

    /**
     * This method updates the consumer, that is called when an engines category gets selected
     *
     * @param onCategorySelection The new consumer to be called
     */
    public void setOnCategorySelection(Consumer<EngineCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }

    public void setOnApplySearchTerm(Consumer<String> onApplySearchTerm) {
        this.onApplySearchTerm = onApplySearchTerm;
    }

    public void setOnSearchTermClear(Runnable onSearchTermClear) {
        this.onSearchTermClear = onSearchTermClear;
    }

    public void setOnApplyInstalledFilter(Consumer<Boolean> onApplyInstalledFilter) {
        this.onApplyInstalledFilter = onApplyInstalledFilter;
    }

    public void setOnApplyUninstalledFilter(Consumer<Boolean> onApplyUninstalledFilter) {
        this.onApplyUninstalledFilter = onApplyUninstalledFilter;
    }
}
