package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import org.phoenicis.javafx.components.skin.SettingsSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsSidebar;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsSidebar.SettingsSidebarItem;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link SettingsSidebar}
 */
public class SettingsSidebarToggleGroup extends
        SidebarToggleGroupBase<SettingsSidebarItem, SettingsSidebarToggleGroup, SettingsSidebarToggleGroupSkin> {
    /**
     * A consumer, which is called when a settings item has been selected
     */
    private final ObjectProperty<Consumer<Node>> onSelectSettingsItem;

    /**
     * Constructor
     *
     * @param title The title of the settings sidebar toggle group
     */
    public SettingsSidebarToggleGroup(String title) {
        super(title);

        this.onSelectSettingsItem = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SettingsSidebarToggleGroupSkin createSkin() {
        return new SettingsSidebarToggleGroupSkin(this);
    }

    public Consumer<Node> getOnSelectSettingsItem() {
        return onSelectSettingsItem.get();
    }

    public ObjectProperty<Consumer<Node>> onSelectSettingsItemProperty() {
        return onSelectSettingsItem;
    }

    public void setOnSelectSettingsItem(Consumer<Node> onSelectSettingsItem) {
        this.onSelectSettingsItem.set(onSelectSettingsItem);
    }
}
