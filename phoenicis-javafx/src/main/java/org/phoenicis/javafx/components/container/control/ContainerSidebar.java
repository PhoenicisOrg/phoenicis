package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.container.skin.ContainerSidebarSkin;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersFilter;

/**
 * A sidebar implementation for the container tab
 */
public class ContainerSidebar
        extends ExtendedSidebarBase<ContainerCategoryDTO, ContainerSidebar, ContainerSidebarSkin> {
    /**
     * The selected container category
     */
    private final ObjectProperty<ContainerCategoryDTO> selectedContainerCategory;

    /**
     * The container filter utility class
     */
    private final ContainersFilter filter;

    /**
     * Constructor
     *
     * @param filter The container filter utility class
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     */
    public ContainerSidebar(ContainersFilter filter, ObservableList<ContainerCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        super(items, filter.searchTermProperty(), selectedListWidget);

        this.filter = filter;

        this.selectedContainerCategory = filter.selectedContainerCategoryProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerSidebarSkin createSkin() {
        return new ContainerSidebarSkin(this);
    }

    public ContainerCategoryDTO getSelectedContainerCategory() {
        return this.selectedContainerCategory.get();
    }

    public ObjectProperty<ContainerCategoryDTO> selectedContainerCategoryProperty() {
        return this.selectedContainerCategory;
    }

    public void setSelectedContainerCategory(ContainerCategoryDTO selectedContainerCategory) {
        this.selectedContainerCategory.set(selectedContainerCategory);
    }

    public ContainersFilter getFilter() {
        return this.filter;
    }
}
