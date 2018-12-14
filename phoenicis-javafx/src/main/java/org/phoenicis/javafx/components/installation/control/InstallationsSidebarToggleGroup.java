package org.phoenicis.javafx.components.installation.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.SidebarToggleGroupBase;
import org.phoenicis.javafx.components.installation.skin.InstallationsSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsSidebar;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;

/**
 * A toggle group component used inside the {@link InstallationsSidebar}
 */
public class InstallationsSidebarToggleGroup extends
        SidebarToggleGroupBase<InstallationCategoryDTO, InstallationsSidebarToggleGroup, InstallationsSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the installations sidebar toggle group
     * @param elements An observable list containing the elements of the sidebar toggle group
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
