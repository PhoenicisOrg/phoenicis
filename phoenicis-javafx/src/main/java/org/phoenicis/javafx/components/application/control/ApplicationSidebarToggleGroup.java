package org.phoenicis.javafx.components.application.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.application.skin.ApplicationSidebarToggleGroupSkin;
import org.phoenicis.javafx.components.common.control.SidebarToggleGroupBase;
import org.phoenicis.repository.dto.CategoryDTO;

/**
 * A toggle group component used inside the {@link ApplicationSidebar}
 */
public class ApplicationSidebarToggleGroup
        extends SidebarToggleGroupBase<CategoryDTO, ApplicationSidebarToggleGroup, ApplicationSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param elements An observable list containing the elements of the sidebar toggle group
     */
    public ApplicationSidebarToggleGroup(ObservableList<CategoryDTO> elements) {
        super(elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationSidebarToggleGroupSkin createSkin() {
        return new ApplicationSidebarToggleGroupSkin(this);
    }
}
