package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.application.skin.ApplicationSidebarToggleGroupSkin;
import org.phoenicis.javafx.components.common.control.SidebarToggleGroupBase;
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
     * @param selectedElement The selected element or null if no/all elements have been selected
     */
    public ApplicationSidebarToggleGroup(String title, ObservableList<CategoryDTO> elements,
            ObjectProperty<CategoryDTO> selectedElement) {
        super(new SimpleStringProperty(title), elements, selectedElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationSidebarToggleGroupSkin createSkin() {
        return new ApplicationSidebarToggleGroupSkin(this);
    }
}
