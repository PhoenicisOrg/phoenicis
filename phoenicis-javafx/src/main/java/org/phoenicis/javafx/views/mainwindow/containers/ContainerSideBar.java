package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * An instance of this class represents the left sidebar of the container tab view.
 * This sidebar contains two items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for a container.
 * </li>
 * <li>
 * A list of buttons containing a button for each container the user has installed.
 * After pressing on such a button information about the selected container is shown on the right side of the window.
 * </li>
 * </ul>
 *
 * @author marc
 * @since 22.04.17
 */
public class ContainerSideBar extends LeftSideBar {
    // the search bar used for filtering
    private SearchBox searchBar;

    // a button group containing a button for each installed container
    private LeftToggleGroup<ContainerCategoryDTO> categoryView;

    // consumer called when a search term is entered
    private Consumer<String> onApplyFilter;

    // consumer called when a container is selected
    private Runnable onAllCategorySelection;
    private Consumer<ContainerCategoryDTO> onCategorySelection = container -> {
    };

    /**
     * Constructor
     */
    public ContainerSideBar() {
        super();

        this.populateSearchBar();
        this.populateCategories();

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.categoryView);
    }

    /**
     * This method selects the "All" application category
     */
    public void selectAllCategories() {
        this.categoryView.selectAll();
    }

    /**
     * This method takes an {@link ObservableList} of container categories and binds it to the container categories button group
     *
     * @param categories The list of container categories
     */
    public void bindCategories(ObservableList<ContainerCategoryDTO> categories) {
        Bindings.bindContent(categoryView.getElements(), categories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(text -> onApplyFilter.accept(text), () -> {
        });
    }

    /**
     * This method populates the button group showing all installed containers
     */
    private void populateCategories() {
        this.categoryView = LeftToggleGroup.create(translate("Containers"), this::createAllCategoriesToggleButton,
                this::createContainerToggleButton);
    }

    /**
     * This method is responsible for creating the "All" categories toggle button.
     *
     * @return The newly created "All" categories toggle button
     */
    private ToggleButton createAllCategoriesToggleButton() {
        final LeftToggleButton allCategoryButton = new LeftToggleButton("All");

        allCategoryButton.setSelected(true);
        allCategoryButton.getStyleClass().add("containerButton");
        allCategoryButton.setOnMouseClicked(event -> onAllCategorySelection.run());

        return allCategoryButton;
    }

    /**
     * This method creates a new toggle button for a given container.
     *
     * @param category The container for which a toggle button should be created
     * @return The created toggle button
     */
    private ToggleButton createContainerToggleButton(ContainerCategoryDTO category) {
        LeftToggleButton containerButton = new LeftToggleButton(category.getName());

        containerButton.getStyleClass().add("containerButton");
        containerButton.setOnMouseClicked(event -> onCategorySelection.accept(category));

        return containerButton;
    }

    /**
     * This method updates the consumer, that is called when a search term has been entered
     *
     * @param onApplyFilter The new consumer to be called
     */
    public void setOnApplyFilter(Consumer<String> onApplyFilter) {
        this.onApplyFilter = onApplyFilter;
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
     * This method updates the consumer, that is called when a container is selected
     *
     * @param onCategorySelection The new consumer to be called
     */
    public void setOnCategorySelection(Consumer<ContainerCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }
}
