package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.views.mainwindow.ui.LeftSpacer;
import org.phoenicis.javafx.views.mainwindow.ui.LeftToggleButton;
import org.phoenicis.javafx.views.mainwindow.ui.LeftToggleGroup;
import org.phoenicis.javafx.views.mainwindow.ui.SearchBox;

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
public class ContainerSideBar extends VBox {
    // the search bar used for filtering
    private SearchBox searchBar;

    // a button group containing a button for each installed container
    private LeftToggleGroup<ContainerDTO> containerView;

    // consumer called when a search term is entered
    private Consumer<String> onApplyFilter;

    // consumer called when a container is selected
    private Consumer<ContainerDTO> onSelectContainer = container -> {};

    /**
     * Constructor
     */
    public ContainerSideBar() {
        super();

        this.populateSearchBar();
        this.populateContainers();

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.containerView);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(filterText -> onApplyFilter.accept(filterText), () -> {});
    }

    /**
     * This method populates the button group showing all installed containers
     */
    private void populateContainers() {
        this.containerView = LeftToggleGroup.create(translate("Containers"), this::createContainerToggleButton);
    }

    /**
     * This method creates a new toggle button for a given container.
     *
     * @param container The container for which a toggle button should be created
     * @return The created toggle button
     */
    private ToggleButton createContainerToggleButton(ContainerDTO container) {
        LeftToggleButton containerButton = new LeftToggleButton(container.getName());

        containerButton.getStyleClass().add("containerButton");
        containerButton.setOnMouseClicked(event -> onSelectContainer.accept(container));

        return containerButton;
    }

    /**
     * This method takes an {@link ObservableList} of containers and binds it to the installed containers button group
     *
     * @param containers
     */
    public void bindContainers(ObservableList<ContainerDTO> containers) {
        Bindings.bindContent(containerView.getElements(), containers);
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
     * This method updates the consumer, that is called when a container is selected
     *
     * @param onSelectContainer The new consumer to be called
     */
    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.onSelectContainer = onSelectContainer;
    }
}
