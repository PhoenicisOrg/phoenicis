package org.phoenicis.javafx.components.setting.utils;

import javafx.scene.Node;

/**
 * This class contains all needed information to display and manage a settings panel
 */
public class SettingsSidebarItem {
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
