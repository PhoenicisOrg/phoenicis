package org.phoenicis.javafx.components.container.control;

import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.components.container.skin.ContainersSidebarToggleGroupSkin;
import org.phoenicis.javafx.components.common.control.SidebarToggleGroupBase;
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
     * @param elements An observable list containing the elements of the sidebar toggle group
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
