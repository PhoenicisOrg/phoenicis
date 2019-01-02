package org.phoenicis.javafx.components.setting.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.setting.skin.SettingsSidebarSkin;
import org.phoenicis.javafx.components.setting.utils.SettingsSidebarItem;

public class SettingsSidebar extends SidebarBase<SettingsSidebarItem, SettingsSidebar, SettingsSidebarSkin> {
    public SettingsSidebar(ObservableList<SettingsSidebarItem> items) {
        super(items);
    }

    @Override
    public SettingsSidebarSkin createSkin() {
        return new SettingsSidebarSkin(this);
    }
}
