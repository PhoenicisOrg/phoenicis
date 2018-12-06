package org.phoenicis.javafx.components.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.skin.ApplicationSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsSidebar;
import org.phoenicis.repository.dto.CategoryDTO;

/**
 * A toggle group component used inside the {@link ApplicationsSidebar}
 */
public class ApplicationSidebarToggleGroup
        extends SidebarToggleGroupBase<CategoryDTO, ApplicationSidebarToggleGroup, ApplicationSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the application sidebar toggle group
     * @param elements An observable list containing the elements of the sidebar toggle group
     */
    public ApplicationSidebarToggleGroup(String title, ObservableList<CategoryDTO> elements) {
        super(title, elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationSidebarToggleGroupSkin createSkin() {
        return new ApplicationSidebarToggleGroupSkin(this);
    }
}
