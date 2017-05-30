package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the left sidebar of the apps tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an application in the selected categories of his/her repositories.
 * </li>
 * <li>
 * A toggle group containing all categories contained in his/her repositories including an "All" category.
 * </li>
 * <li>
 * A filter group, containing filters to be used to remove testing, no cd needed and
 * commercial applications from the shown applications
 * </li>
 * </ul>
 *
 * @author marc
 * @since 21.04.17
 */
public class ApplicationSideBar extends LeftSideBar {
    private final ApplicationFilter filter;

    // the search bar user for application filtering/searching
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private LeftScrollPane centerContent;

    // the toggleable categories
    private LeftToggleGroup<CategoryDTO> categoryView;

    // the group containing the application filters (testing, noCdNeeded and commercial)
    private LeftGroup filterGroup;

    private CheckBox testingCheck;
    private CheckBox noCdNeededCheck;
    private CheckBox commercialCheck;

    // widget to switch between the different list widgets in the center view
    private LeftListWidgetChooser<ApplicationDTO> listWidgetChooser;

    // consumers called when an action inside the searchBar has been performed (a filter text was entered or the filter has been cleared)
    private Consumer<String> onFilterTextEnter;
    private Runnable onFilterClear;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<CategoryDTO> onCategorySelection;

    /**
     * Constructor
     *
     * @param combinedListWidget The list widget to be managed by the ListWidgetChooser in the sidebar
     */
    public ApplicationSideBar(CombinedListWidget<ApplicationDTO> combinedListWidget, ApplicationFilter filter) {
        super();

        this.filter = filter;

        this.populateSearchBar();
        this.populateCategories();
        this.populateFilters();
        this.populateListWidgetChooser(combinedListWidget);

        this.centerContent = new LeftScrollPane(this.categoryView, new LeftSpacer(), this.filterGroup);

        this.setTop(this.searchBar);
        this.setCenter(this.centerContent);
        this.setBottom(this.listWidgetChooser);
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
        Bindings.bindContent(categoryView.getElements(), categories);
    }

    private void populateSearchBar() {
        this.searchBar = new SearchBox(text -> this.onFilterTextEnter.accept(text), () -> this.onFilterClear.run());
    }

    private void populateCategories() {
        this.categoryView = LeftToggleGroup.create(tr("Categories"), this::createAllCategoriesToggleButton,
                this::createCategoryToggleButton);
    }

    private void populateFilters() {
        this.testingCheck = new LeftCheckBox(tr("Testing"));
        this.testingCheck.selectedProperty().bindBidirectional(filter.containTestingApplicationsProperty());

        this.noCdNeededCheck = new LeftCheckBox(tr("No CD needed"));
        this.noCdNeededCheck.selectedProperty().bindBidirectional(filter.containNoCDApplicationsProperty());

        this.commercialCheck = new LeftCheckBox(tr("Commercial"));
        this.commercialCheck.selectedProperty().bindBidirectional(filter.containCommercialApplicationsProperty());
        this.commercialCheck.setSelected(true);

        this.filterGroup = new LeftGroup("Filters", testingCheck, noCdNeededCheck, commercialCheck);
    }

    /**
     * This method populates the list widget choose
     *
     * @param combinedListWidget The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<ApplicationDTO> combinedListWidget) {
        this.listWidgetChooser = new LeftListWidgetChooser<>(combinedListWidget);
        this.listWidgetChooser.setAlignment(Pos.BOTTOM_LEFT);
    }

    /**
     * This method is responsible for creating the "All" categories toggle button.
     *
     * @return The newly created "All" categories toggle button
     */
    private ToggleButton createAllCategoriesToggleButton() {
        final LeftToggleButton allCategoryButton = new LeftToggleButton("All");

        allCategoryButton.setSelected(true);
        allCategoryButton.setId("allButton");
        allCategoryButton.setOnMouseClicked(event -> onAllCategorySelection.run());

        return allCategoryButton;
    }

    /**
     * This method is responsible for creating a toggle button for a given category.
     *
     * @param category The category for which a toggle button should be created
     * @return The newly created toggle button
     */
    private ToggleButton createCategoryToggleButton(CategoryDTO category) {
        final LeftToggleButton categoryButton = new LeftToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getName().toLowerCase()));
        categoryButton.setOnMouseClicked(event -> onCategorySelection.accept(category));

        return categoryButton;
    }

    /**
     * This method sets the consumer, that is called after a search term has been entered in the search bar.
     *
     * @param onFilterTextEnter The new consumer to be used
     */
    public void setOnFilterTextEnter(Consumer<String> onFilterTextEnter) {
        this.onFilterTextEnter = onFilterTextEnter;
    }

    /**
     * This method sets the consumer, that is called after the "clear" button in the search bar has been clicked.
     *
     * @param onFilterClear The new consumer to be used
     */
    public void setOnFilterClear(Runnable onFilterClear) {
        this.onFilterClear = onFilterClear;
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
