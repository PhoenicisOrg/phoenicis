package org.phoenicis.javafx.components.container.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ScrollPane;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.components.container.control.ContainerSidebar;
import org.phoenicis.javafx.components.container.control.ContainersSidebarToggleGroup;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerSidebar} component
 */
public class ContainerSidebarSkin
        extends ExtendedSidebarSkinBase<ContainerCategoryDTO, ContainerSidebar, ContainerSidebarSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerSidebarSkin(ContainerSidebar control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScrollPane createMainContent() {
        return createScrollPane(createSidebarToggleGroup());
    }

    /**
     * Creates a {@link ContainersSidebarToggleGroup} object containing all installed containers
     */
    private ContainersSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<ContainerCategoryDTO> filteredContainerCategories = getControl().getItems()
                .filtered(getControl().getFilter()::filter);

        filteredContainerCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().searchTermProperty(),
                        getControl().selectedContainerCategoryProperty()));

        final ContainersSidebarToggleGroup sidebarToggleGroup = new ContainersSidebarToggleGroup(tr("Containers"),
                filteredContainerCategories);

        getControl().selectedContainerCategoryProperty().bind(sidebarToggleGroup.selectedElementProperty());

        return sidebarToggleGroup;
    }

}
