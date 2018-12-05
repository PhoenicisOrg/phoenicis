package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.components.control.ContainersSidebarToggleGroup;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.DelayedFilterTextConsumer;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarScrollPane;

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
    private ContainersSidebarToggleGroup categoryView;

    // widget to switch between the different list widgets in the center view
    private ListWidgetSelector listWidgetSelector;

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
        this.setBottom(listWidgetSelector);
    }

    /**
     * This method takes an {@link ObservableList} of container categories and binds it to the container categories
     * button group
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

        this.categoryView = new ContainersSidebarToggleGroup(tr("Containers"));

        this.categoryView.setOnAllCategorySelection(() -> onAllCategorySelection.run());
        this.categoryView.setOnCategorySelection(container -> onCategorySelection.accept(container));

        Bindings.bindContent(categoryView.getElements(), filteredContainerCategories);
    }

    /**
     * This method populates the list widget choose
     *
     * @param availableContainers The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<ContainerDTO> availableContainers) {
        this.listWidgetSelector = new ListWidgetSelector();
        this.listWidgetSelector.setSelected(this.javaFxSettingsManager.getContainersListType());
        this.listWidgetSelector.setOnSelect(type -> {
            availableContainers.showList(type);

            this.javaFxSettingsManager.setContainersListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    /**
     * Filters the containers for a given search term
     *
     * @param searchTerm The keyword to search for
     */
    public void search(String searchTerm) {
        this.filter.setSearchTerm(searchTerm);
    }

    /**
     * Clears the search term of the filter function
     */
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
