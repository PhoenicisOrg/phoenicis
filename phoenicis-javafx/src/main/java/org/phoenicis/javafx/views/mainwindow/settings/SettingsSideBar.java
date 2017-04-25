package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.mainwindow.ui.LeftToggleButton;
import org.phoenicis.javafx.views.mainwindow.ui.LeftToggleGroup;

import java.util.function.Consumer;

/**
 * An instance of this class represents the sidebar inside the settings tab view.
 * This sidebar contains a toggle button group used to select the different types of settings.
 *
 * @author marc
 * @since 23.04.17
 */
public class SettingsSideBar extends VBox {
    // the toggle button group containing the buttons used to navigate to the different setting panels
    private LeftToggleGroup<SettingsSideBarItem> settingsItems;

    // consumer called when a settings toggle button has been clicked
    private Consumer<VBox> onSelectSettingsItem;

    /**
     * Constructor
     */
    public SettingsSideBar() {
        super();

        this.populate();

        this.getChildren().addAll(this.settingsItems);
    }

    /**
     * This method populates the toggle button group containing a toggle button for each settings panel
     */
    private void populate() {
        this.settingsItems = LeftToggleGroup.create("Settings", this::createSettingsToggleButton);
    }

    /**
     * This method creates a toggle button for a given settings panel.
     *
     * @param item The settings panel together with its displayed name and icon css class
     * @return The created toggle button
     */
    private ToggleButton createSettingsToggleButton(SettingsSideBarItem item) {
        ToggleButton toggleButton = new LeftToggleButton(item.getName());

        toggleButton.getStyleClass().add(item.getIconClass());
        toggleButton.setOnAction(event -> onSelectSettingsItem.accept(item.getPanel()));

        return toggleButton;
    }

    /**
     * This method binds the given settings panels to the toggle button group inside this sidebar
     *
     * @param items The settings toggle buttons
     */
    public void bindSettingsItems(ObservableList<SettingsSideBarItem> items) {
        Bindings.bindContent(this.settingsItems.getElements(), items);
    }

    /**
     * This method selects the first settings category
     */
    public void selectFirstSettingsCategory() {
        this.settingsItems.select(0);
    }

    /**
     * This method updates the consumer that is called when a settings toggle button has been clicked
     *
     * @param onSelectSettingsItem The new consumer to be called
     */
    public void setOnSelectSettingsItem(Consumer<VBox> onSelectSettingsItem) {
        this.onSelectSettingsItem = onSelectSettingsItem;
    }

    /**
     * This class contains all needed information to display and manage a settings panel
     */
    public static class SettingsSideBarItem {
        // the corresponding panel for this settings category
        private final VBox panel;
        // the css class containing the icon for this settings category
        private final String iconClass;
        // the displayed name of this settings category
        private final String name;

        /**
         * Constructor
         *
         * @param panel     The corresponding panel for this settings category
         * @param iconClass The css class containing the icon for this settings category
         * @param name      The displayed name of this settings category
         */
        public SettingsSideBarItem(VBox panel, String iconClass, String name) {
            this.panel = panel;
            this.iconClass = iconClass;
            this.name = name;
        }

        public VBox getPanel() {
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
