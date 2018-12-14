package org.phoenicis.javafx.components.control;

import javafx.collections.ObservableList;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.skin.EnginesSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsSidebar;

/**
 * A toggle group component used inside the {@link ApplicationsSidebar}
 */
public class EnginesSidebarToggleGroup
        extends SidebarToggleGroupBase<EngineCategoryDTO, EnginesSidebarToggleGroup, EnginesSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the engines sidebar toggle group
     * @param elements An observable list containing the elements of the sidebar toggle group
     */
    public EnginesSidebarToggleGroup(String title, ObservableList<EngineCategoryDTO> elements) {
        super(title, elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnginesSidebarToggleGroupSkin createSkin() {
        return new EnginesSidebarToggleGroupSkin(this);
    }
}
