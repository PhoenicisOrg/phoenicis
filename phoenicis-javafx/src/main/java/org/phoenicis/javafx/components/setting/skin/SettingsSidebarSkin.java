package org.phoenicis.javafx.components.setting.skin;

import javafx.scene.control.ScrollPane;
import org.phoenicis.javafx.components.common.skin.SidebarSkinBase;
import org.phoenicis.javafx.components.setting.control.SettingsSidebar;
import org.phoenicis.javafx.components.setting.control.SettingsSidebarToggleGroup;
import org.phoenicis.javafx.components.setting.utils.SettingsSidebarItem;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The skin for the {@link SettingsSidebar} component
 */
public class SettingsSidebarSkin extends SidebarSkinBase<SettingsSidebarItem, SettingsSidebar, SettingsSidebarSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public SettingsSidebarSkin(SettingsSidebar control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScrollPane createMainContent() {
        final SettingsSidebarToggleGroup sidebarToggleGroup = new SettingsSidebarToggleGroup(
                tr("Settings"), getControl().getItems());

        getControl().selectedItemProperty().bindBidirectional(sidebarToggleGroup.selectedElementProperty());

        return createScrollPane(sidebarToggleGroup);
    }
}
