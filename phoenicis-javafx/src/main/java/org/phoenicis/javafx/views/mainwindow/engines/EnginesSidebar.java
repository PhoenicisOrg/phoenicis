package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.components.control.EnginesSidebarToggleGroup;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.components.control.SidebarGroup;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.DelayedFilterTextConsumer;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarCheckBox;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarScrollPane;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarSpacer;

import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the engines tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an engine.
 * </li>
 * <li>
 * A button group containing a button for all known engine groups.
 * After pressing on one such button all engines belonging to the selected engine group are shown in the main window
 * panel.
 * </li>
 * <li>
 * A button group containing buttons to filter for installed and uninstalled engines.
 * </li>
 * </ul>
 *
 * @author marc
 * @since 22.04.17
 */
public class EnginesSidebar extends Sidebar {
    private final EnginesFilter filter;

    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private SidebarScrollPane centerContent;

    private ObservableList<EngineCategoryDTO> engineCategories;
    private PhoenicisFilteredList<EngineCategoryDTO> filteredEngineCategories;

    // the button group containing a button for all engine categories
    private EnginesSidebarToggleGroup categoryView;

    // the button group containing a button to filter the engines for installed and uninstalled engines
    private SidebarGroup<CheckBox> installationFilterGroup;

    private CheckBox installedCheck;
    private CheckBox notInstalledCheck;

    // widget to switch between the different list widgets in the center view
    private ListWidgetSelector listWidgetSelector;

    // consumer called when a category has been selected
    private Consumer<EngineCategoryDTO> onCategorySelection;

    private final JavaFxSettingsManager javaFxSettingsManager;

    /**
     * Constructor
     *
     * @param enginesVersionListWidgets The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public EnginesSidebar(List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets, EnginesFilter filter,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateEngineCategories();
        this.populateInstallationFilters();
        this.populateListWidgetChooser(enginesVersionListWidgets);

        this.centerContent = new SidebarScrollPane(this.categoryView, new SidebarSpacer(),
                this.installationFilterGroup);

        this.setTop(searchBar);
        this.setCenter(centerContent);
        this.setBottom(listWidgetSelector);
    }

    /**
     * This method takes an {@link ObservableList} of engine categories and binds it to the engine categories button
     * group
     *
     * @param engineCategories The list of engine categories
     */
    public void bindEngineCategories(ObservableList<EngineCategoryDTO> engineCategories) {
        Bindings.bindContent(this.engineCategories, engineCategories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(new DelayedFilterTextConsumer(this::search), this::clearSearch);
    }

    /**
     * This method populates the button group showing all known engine categories
     */
    private void populateEngineCategories() {
        this.engineCategories = FXCollections.observableArrayList();
        this.filteredEngineCategories = new PhoenicisFilteredList<>(engineCategories, filter::filter);
        this.filter.addOnFilterChanged(filteredEngineCategories::trigger);

        this.categoryView = new EnginesSidebarToggleGroup(tr("Engines"));

        this.categoryView.setOnCategorySelection(category -> onCategorySelection.accept(category));

        Bindings.bindContent(categoryView.getElements(), filteredEngineCategories);
    }

    /**
     * This method populates the button group containing buttons to filter for installed and not installed engines
     */
    private void populateInstallationFilters() {
        this.installedCheck = new SidebarCheckBox(tr("Installed"));
        this.installedCheck.selectedProperty().bindBidirectional(filter.showInstalledProperty());
        this.installedCheck.setSelected(true);

        this.notInstalledCheck = new SidebarCheckBox(tr("Not installed"));
        this.notInstalledCheck.selectedProperty().bindBidirectional(filter.showNotInstalledProperty());
        this.notInstalledCheck.setSelected(true);

        this.installationFilterGroup = new SidebarGroup<>();
        this.installationFilterGroup.getComponents().addAll(installedCheck, notInstalledCheck);
    }

    /**
     * This method populates the list widget choose
     *
     * @param enginesVersionListWidgets The managed CombinedListWidgets
     */
    private void populateListWidgetChooser(List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets) {
        this.listWidgetSelector = new ListWidgetSelector();
        this.listWidgetSelector.setSelected(this.javaFxSettingsManager.getEnginesListType());
        this.listWidgetSelector.setOnSelect(type -> {
            enginesVersionListWidgets.forEach(widget -> widget.showList(type));

            this.javaFxSettingsManager.setEnginesListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    /**
     * Filters the engines and engine categories for a given keyword
     *
     * @param searchTerm The keyword to search for
     */
    public void search(String searchTerm) {
        this.filter.setSearchTerm(searchTerm);
    }

    /**
     * Clears the keyword of the filter function
     */
    public void clearSearch() {
        this.filter.clearSearchTerm();
    }

    /**
     * This method updates the consumer, that is called when an engines category gets selected
     *
     * @param onCategorySelection The new consumer to be called
     */
    public void setOnCategorySelection(Consumer<EngineCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }
}
