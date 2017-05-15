package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

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
    private CombinedListWidget<ApplicationDTO> combinedListWidget;

    private BorderPane borderPane;

    // the search bar user for application filtering/searching
    private SearchBox searchBar;

    // the toggleable categories
    private LeftToggleGroup<CategoryDTO> categoryView;

    // the group containing the application filters (testing, noCdNeeded and commercial)
    private LeftGroup filterGroup;

    private CheckBox testingCheck;
    private CheckBox noCdNeededCheck;
    private CheckBox commercialCheck;

    private LeftListWidgetChooser<ApplicationDTO> listWidgetChooser;

    // consumers called when an action inside the searchBar has been performed (a filter text was entered or the filter has been cleared)
    private Consumer<String> onFilterTextEnter;
    private Runnable onFilterClear;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<CategoryDTO> onCategorySelection;

    public ApplicationSideBar(CombinedListWidget<ApplicationDTO> combinedListWidget) {
        super();

        this.combinedListWidget = combinedListWidget;

        this.populateSearchBar();
        this.populateCategories();
        this.populateFilters();
        this.populateListWidgetChooser();

        this.borderPane = new BorderPane();
        this.borderPane.setPrefHeight(0);

        this.borderPane.setTop(this.searchBar);

        VBox center = new VBox(new LeftSpacer(), this.categoryView, new LeftSpacer(), this.filterGroup);

        this.borderPane.setCenter(center);
        this.borderPane.setBottom(this.listWidgetChooser);

        this.getChildren().setAll(this.borderPane);

        VBox.setVgrow(borderPane, Priority.ALWAYS);
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
        this.categoryView = LeftToggleGroup.create(translate("Categories"), this::createAllCategoriesToggleButton,
                this::createCategoryToggleButton);
    }

    private void populateFilters() {
        this.testingCheck = new LeftCheckBox(translate("Testing"));
        this.noCdNeededCheck = new LeftCheckBox(translate("No CD needed"));
        this.commercialCheck = new LeftCheckBox(translate("Commercial"));

        this.filterGroup = new LeftGroup("Filters", testingCheck, noCdNeededCheck, commercialCheck);
    }

    private void populateListWidgetChooser() {
        this.listWidgetChooser = new LeftListWidgetChooser<>(combinedListWidget);
        this.listWidgetChooser.setPadding(new Insets(5));
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
