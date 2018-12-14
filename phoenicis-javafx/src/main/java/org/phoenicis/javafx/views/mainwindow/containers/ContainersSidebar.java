package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.components.control.ContainersSidebarToggleGroup;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarScrollPane;

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

    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObservableList<ContainerCategoryDTO> containerCategories;

    /**
     * Constructor
     *
     * @param availableContainers The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public ContainersSidebar(ContainersFilter filter, JavaFxSettingsManager javaFxSettingsManager,
            ObservableList<ContainerCategoryDTO> containerCategories,
            CombinedListWidget<ContainerDTO> availableContainers) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.containerCategories = containerCategories;

        initialise(availableContainers);
    }

    private void initialise(CombinedListWidget<ContainerDTO> availableContainers) {
        SearchBox searchBox = createSearchBox();
        ContainersSidebarToggleGroup sidebarToggleGroup = createSidebarToggleGroup();
        ListWidgetSelector createListWidgetSelector = createListWidgetSelector(availableContainers);

        setTop(searchBox);
        setCenter(new SidebarScrollPane(sidebarToggleGroup));
        setBottom(createListWidgetSelector);
    }

    /**
     * This method populates the searchbar
     */
    private SearchBox createSearchBox() {
        SearchBox searchBox = new SearchBox();

        filter.searchTermProperty().bind(searchBox.searchTermProperty());

        return searchBox;
    }

    /**
     * This method populates the button group showing all installed containers
     */
    private ContainersSidebarToggleGroup createSidebarToggleGroup() {
        FilteredList<ContainerCategoryDTO> filteredContainerCategories = containerCategories.filtered(filter::filter);

        filteredContainerCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter,
                        filter.searchTermProperty(),
                        filter.selectedContainerCategoryProperty()));

        ContainersSidebarToggleGroup sidebarToggleGroup = new ContainersSidebarToggleGroup(tr("Containers"),
                filteredContainerCategories);

        filter.selectedContainerCategoryProperty().bind(sidebarToggleGroup.selectedElementProperty());

        return sidebarToggleGroup;
    }

    /**
     * This method populates the list widget choose
     *
     * @param availableContainers The managed CombinedListWidget
     */
    private ListWidgetSelector createListWidgetSelector(CombinedListWidget<ContainerDTO> availableContainers) {
        ListWidgetSelector listWidgetSelector = new ListWidgetSelector();

        listWidgetSelector.setSelected(javaFxSettingsManager.getContainersListType());
        listWidgetSelector.setOnSelect(type -> {
            availableContainers.showList(type);

            javaFxSettingsManager.setContainersListType(type);
            javaFxSettingsManager.save();
        });

        return listWidgetSelector;
    }
}
