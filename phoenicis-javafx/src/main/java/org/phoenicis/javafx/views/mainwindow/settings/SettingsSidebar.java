package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.phoenicis.javafx.components.control.SettingsSidebarToggleGroup;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar inside the settings tab view.
 * This sidebar contains a toggle button group used to select the different types of settings.
 *
 * @author marc
 * @since 23.04.17
 */
public class SettingsSidebar extends Sidebar {
    // the toggle button group containing the buttons used to navigate to the different setting panels
    private SettingsSidebarToggleGroup settingsItems;

    // consumer called when a settings toggle button has been clicked
    private Consumer<Node> onSelectSettingsItem;

    /**
     * Constructor
     */
    public SettingsSidebar() {
        super();

        this.populate();

        this.setCenter(this.settingsItems);
    }

    /**
     * This method populates the toggle button group containing a toggle button for each settings panel
     */
    private void populate() {
        this.settingsItems = new SettingsSidebarToggleGroup(tr("Settings"));

        this.settingsItems.setOnSelectSettingsItem(item -> onSelectSettingsItem.accept(item));
    }

    /**
     * This method binds the given settings panels to the toggle button group inside this sidebar
     *
     * @param items The settings toggle buttons
     */
    public void bindSettingsItems(ObservableList<SettingsSidebarItem> items) {
        Bindings.bindContent(this.settingsItems.getElements(), items);
    }

    /**
     * This method updates the consumer that is called when a settings toggle button has been clicked
     *
     * @param onSelectSettingsItem The new consumer to be called
     */
    public void setOnSelectSettingsItem(Consumer<Node> onSelectSettingsItem) {
        this.onSelectSettingsItem = onSelectSettingsItem;
    }

    /**
     * This class contains all needed information to display and manage a settings panel
     */
    public static class SettingsSidebarItem {
        // the corresponding panel for this settings category
        private final Node panel;
        // the css class containing the icon for this settings category
        private final String iconClass;
        // the displayed name of this settings category
        private final String name;

        /**
         * Constructor
         *
         * @param panel The corresponding panel for this settings category
         * @param iconClass The css class containing the icon for this settings category
         * @param name The displayed name of this settings category
         */
        public SettingsSidebarItem(Node panel, String iconClass, String name) {
            this.panel = panel;
            this.iconClass = iconClass;
            this.name = name;
        }

        public Node getPanel() {
            return panel;
        }

        public String getIconClass() {
            return iconClass;
        }

        public String getName() {
            return name;
        }
    }
}
