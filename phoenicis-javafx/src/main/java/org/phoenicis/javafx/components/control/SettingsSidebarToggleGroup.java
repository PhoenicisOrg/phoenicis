package org.phoenicis.javafx.components.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.skin.SettingsSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsSidebar;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsSidebar.SettingsSidebarItem;

/**
 * A toggle group component used inside the {@link SettingsSidebar}
 */
public class SettingsSidebarToggleGroup extends
        SidebarToggleGroupBase<SettingsSidebarItem, SettingsSidebarToggleGroup, SettingsSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the settings sidebar toggle group
     */
    public SettingsSidebarToggleGroup(String title, ObservableList<SettingsSidebarItem> elements) {
        super(title, elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SettingsSidebarToggleGroupSkin createSkin() {
        return new SettingsSidebarToggleGroupSkin(this);
    }

}
