package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.skin.EnginesSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsSidebar;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link ApplicationsSidebar}
 */
public class EnginesSidebarToggleGroup
        extends SidebarToggleGroupBase<EngineCategoryDTO, EnginesSidebarToggleGroup, EnginesSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the engines sidebar toggle group
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
