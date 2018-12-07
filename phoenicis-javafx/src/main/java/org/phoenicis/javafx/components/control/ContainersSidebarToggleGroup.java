package org.phoenicis.javafx.components.control;

import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.components.skin.ContainersSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersSidebar;

/**
 * A toggle group component used inside the {@link ContainersSidebar}
 */
public class ContainersSidebarToggleGroup extends
        SidebarToggleGroupBase<ContainerCategoryDTO, ContainersSidebarToggleGroup, ContainersSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the containers sidebar toggle group
     */
    public ContainersSidebarToggleGroup(String title, ObservableList<ContainerCategoryDTO> elements) {
        super(title, elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainersSidebarToggleGroupSkin createSkin() {
        return new ContainersSidebarToggleGroupSkin(this);
    }
}
