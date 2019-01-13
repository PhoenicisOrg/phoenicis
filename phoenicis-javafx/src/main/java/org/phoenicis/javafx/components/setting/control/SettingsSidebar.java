package org.phoenicis.javafx.components.setting.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.setting.skin.SettingsSidebarSkin;
import org.phoenicis.javafx.components.setting.utils.SettingsSidebarItem;

/**
 * A sidebar implementation for the settings tab
 */
public class SettingsSidebar extends SidebarBase<SettingsSidebarItem, SettingsSidebar, SettingsSidebarSkin> {
    /**
     * Constructor
     *
     * @param items The items shown inside a toggle button group in the sidebar
     */
    public SettingsSidebar(ObservableList<SettingsSidebarItem> items) {
        super(items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SettingsSidebarSkin createSkin() {
        return new SettingsSidebarSkin(this);
    }
}
