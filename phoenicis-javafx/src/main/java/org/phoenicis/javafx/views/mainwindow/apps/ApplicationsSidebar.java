package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.DelayedFilterTextConsumer;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.*;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the apps tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an application in the selected categories of his/her repositories.
 * </li>
 * <li>
 * A toggle group containing all categories contained in his/her repositories including an "All" category.
 * </li>
 * <li>
 * A filter group, containing filters to be used to remove testing, requires patch and
 * commercial applications from the shown applications
 * </li>
 * </ul>
 *
 * @author marc
 * @since 21.04.17
 */
public class ApplicationsSidebar extends Sidebar {
    private final ApplicationFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    // the search bar user for application filtering/searching
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private SidebarScrollPane centerContent;

    private ObservableList<CategoryDTO> categories;
    private PhoenicisFilteredList<CategoryDTO> filteredCategories;

    // the toggleable categories
    private SidebarToggleGroup<CategoryDTO> categoryView;

    // the group containing the application filters (testing, noCdNeeded and commercial)
    private SidebarGroup filterGroup;

    private CheckBox testingCheck;
    private CheckBox requiresPatchCheck;
    private CheckBox commercialCheck;
    private CheckBox operatingSystemCheck;

    // widget to switch between the different list widgets in the center view
    private ListWidgetSelector listWidgetSelector;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<CategoryDTO> onCategorySelection;

    /**
     * Constructor
     *
     * @param combinedListWidget The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public ApplicationsSidebar(CombinedListWidget<ApplicationDTO> combinedListWidget, ApplicationFilter filter,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateCategories();
        this.populateFilters();
        this.populateListWidgetChooser(combinedListWidget);

        this.centerContent = new SidebarScrollPane(this.categoryView, new SidebarSpacer(), this.filterGroup);

        this.setTop(this.searchBar);
        this.setCenter(this.centerContent);
        this.setBottom(this.listWidgetSelector);
    }

    /**
     * This method selects the "All" application category
     */
    public void selectAllCategories() {
        this.categoryView.selectAll();
    }

    /**
     * This method binds the given category list <code>categories</code> to the categories toggle group.
     *
     * @param categories The to be bound category list
     */
    public void bindCategories(ObservableList<CategoryDTO> categories) {
        Bindings.bindContent(this.categories, categories);
    }

    private void populateSearchBar() {
        this.searchBar = new SearchBox(new DelayedFilterTextConsumer(filter::setFilterText),
                () -> filter.setFilterText(""));
    }

    private void populateCategories() {
        this.categories = FXCollections.observableArrayList();
        this.filteredCategories = new PhoenicisFilteredList<>(categories, filter::filter);
        this.filter.addOnFilterChanged(filteredCategories::trigger);

        this.categoryView = SidebarToggleGroup.create(tr("Categories"), this::createAllCategoriesToggleButton,
                this::createCategoryToggleButton);

        Bindings.bindContent(categoryView.getElements(), filteredCategories);
    }

    private void populateFilters() {
        this.testingCheck = new SidebarCheckBox(tr("Testing"));
        this.testingCheck.selectedProperty().bindBidirectional(filter.containTestingApplicationsProperty());

        this.requiresPatchCheck = new SidebarCheckBox(tr("Patch required"));
        this.requiresPatchCheck.selectedProperty().bindBidirectional(filter.containRequiresPatchApplicationsProperty());

        this.commercialCheck = new SidebarCheckBox(tr("Commercial"));
        this.commercialCheck.selectedProperty().bindBidirectional(filter.containCommercialApplicationsProperty());
        this.commercialCheck.setSelected(true);

        this.operatingSystemCheck = new SidebarCheckBox(tr("All Operating Systems"));
        this.operatingSystemCheck.selectedProperty().bindBidirectional(filter.containAllOSCompatibleApplications());
        this.operatingSystemCheck.setSelected(false);

        this.filterGroup = new SidebarGroup("Filters", testingCheck, requiresPatchCheck, commercialCheck,
                operatingSystemCheck);
    }

    /**
     * This method populates the list widget choose
     *
     * @param combinedListWidget The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<ApplicationDTO> combinedListWidget) {
        this.listWidgetSelector = new ListWidgetSelector();
        this.listWidgetSelector.setSelected(this.javaFxSettingsManager.getAppsListType());
        this.listWidgetSelector.setOnSelect(type -> {
            combinedListWidget.showList(type);

            this.javaFxSettingsManager.setAppsListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    /**
     * This method is responsible for creating the "All" categories toggle button.
     *
     * @return The newly created "All" categories toggle button
     */
    private ToggleButton createAllCategoriesToggleButton() {
        final SidebarToggleButton allCategoryButton = new SidebarToggleButton(tr("All"));

        allCategoryButton.setSelected(true);
        allCategoryButton.setId("allButton");
        allCategoryButton.setOnAction(event -> onAllCategorySelection.run());

        return allCategoryButton;
    }

    /**
     * This method is responsible for creating a toggle button for a given category.
     *
     * @param category The category for which a toggle button should be created
     * @return The newly created toggle button
     */
    private ToggleButton createCategoryToggleButton(CategoryDTO category) {
        final SidebarToggleButton categoryButton = new SidebarToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getId().toLowerCase()));
        categoryButton.setOnAction(event -> onCategorySelection.accept(category));

        return categoryButton;
    }

    /**
     * This method sets the consumer, that is called after a category has been selected
     *
     * @param onAllCategorySelection The new consumer to be used
     */
    public void setOnAllCategorySelection(Runnable onAllCategorySelection) {
        this.onAllCategorySelection = onAllCategorySelection;
    }

    /**
     * This method sets the consumer, that is called after the "All" categories toggle button has been selected
     *
     * @param onCategorySelection The new consumer to be used
     */
    public void setOnCategorySelection(Consumer<CategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }
}
