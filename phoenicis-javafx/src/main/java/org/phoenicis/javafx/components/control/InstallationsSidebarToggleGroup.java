package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.skin.InstallationsSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsSidebar;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link InstallationsSidebar}
 */
public class InstallationsSidebarToggleGroup extends
        SidebarToggleGroupBase<InstallationCategoryDTO, InstallationsSidebarToggleGroup, InstallationsSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the installations sidebar toggle group
     */
    public InstallationsSidebarToggleGroup(String title, ObservableList<InstallationCategoryDTO> elements) {
        super(title, elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallationsSidebarToggleGroupSkin createSkin() {
        return new InstallationsSidebarToggleGroupSkin(this);
    }
}
