package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.DelayedFilterTextConsumer;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the container tab view.
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
public class ContainersSidebar extends Sidebar {
    private final ContainersFilter filter;

    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private SidebarScrollPane centerContent;

    private ObservableList<ContainerCategoryDTO> containerCategories;
    private PhoenicisFilteredList<ContainerCategoryDTO> filteredContainerCategories;

    // a button group containing a button for each installed container
    private SidebarToggleGroup<ContainerCategoryDTO> categoryView;

    // widget to switch between the different list widgets in the center view
    private ListWidgetChooser<ContainerDTO> listWidgetChooser;

    // consumer called when a container is selected
    private Runnable onAllCategorySelection;
    private Consumer<ContainerCategoryDTO> onCategorySelection = container -> {
    };

    private final JavaFxSettingsManager javaFxSettingsManager;

    /**
     * Constructor
     *
     * @param availableContainers The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public ContainersSidebar(CombinedListWidget<ContainerDTO> availableContainers, ContainersFilter filter,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateCategories();
        this.populateListWidgetChooser(availableContainers);

        this.centerContent = new SidebarScrollPane(categoryView);

        this.setTop(searchBar);
        this.setCenter(centerContent);
        this.setBottom(listWidgetChooser);
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
        Bindings.bindContent(this.containerCategories, categories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(new DelayedFilterTextConsumer(this::search), this::clearSearch);
    }

    /**
     * This method populates the button group showing all installed containers
     */
    private void populateCategories() {
        this.containerCategories = FXCollections.observableArrayList();
        this.filteredContainerCategories = new PhoenicisFilteredList<>(this.containerCategories, filter::filter);
        this.filter.addOnFilterChanged(filteredContainerCategories::trigger);

        this.categoryView = SidebarToggleGroup.create(tr("Containers"), this::createAllCategoriesToggleButton,
                this::createContainerToggleButton);
        Bindings.bindContent(categoryView.getElements(), filteredContainerCategories);
    }

    /**
     * This method populates the list widget choose
     *
     * @param availableContainers The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<ContainerDTO> availableContainers) {
        this.listWidgetChooser = new ListWidgetChooser<>(availableContainers);
        this.listWidgetChooser.setAlignment(Pos.BOTTOM_LEFT);
        this.listWidgetChooser.choose(this.javaFxSettingsManager.getContainersListType());
        this.listWidgetChooser.setOnChoose(type -> {
            this.javaFxSettingsManager.setContainersListType(type);
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
        SidebarToggleButton containerButton = new SidebarToggleButton(category.getName());

        containerButton.getStyleClass().add("containerButton");
        containerButton.setOnMouseClicked(event -> onCategorySelection.accept(category));

        return containerButton;
    }

    public void search(String searchTerm) {
        this.filter.setSearchTerm(searchTerm);
    }

    public void clearSearch() {
        this.filter.clearSearchTerm();
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
