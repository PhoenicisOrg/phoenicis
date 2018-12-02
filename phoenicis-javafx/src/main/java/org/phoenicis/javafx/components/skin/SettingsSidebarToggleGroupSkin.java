package org.phoenicis.javafx.components.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.control.SettingsSidebarToggleGroup;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsSidebar;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsSidebar.SettingsSidebarItem;

import java.util.Optional;

/**
 * A {@link SidebarToggleGroupSkinBase} implementation class used inside the {@link SettingsSidebar}
 */
public class SettingsSidebarToggleGroupSkin extends
        SidebarToggleGroupSkinBase<SettingsSidebarItem, SettingsSidebarToggleGroup, SettingsSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public SettingsSidebarToggleGroupSkin(SettingsSidebarToggleGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<ToggleButton> createAllButton() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(SettingsSidebarItem item) {
        ToggleButton toggleButton = createSidebarToggleButton(item.getName());

        toggleButton.getStyleClass().add(item.getIconClass());
        toggleButton.setOnAction(event -> getControl().getOnSelectSettingsItem().accept(item.getPanel()));

        return toggleButton;
    }
}
