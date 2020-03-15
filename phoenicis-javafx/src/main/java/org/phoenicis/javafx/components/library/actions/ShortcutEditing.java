package org.phoenicis.javafx.components.library.actions;

import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.library.dto.ShortcutDTO;

/**
 * Indicator to open the "Shortcut Editing" details panel
 */
public class ShortcutEditing implements OpenDetailsPanel {
    /**
     * The shortcut whose details are to be edited
     */
    private final ShortcutDTO shortcut;

    public ShortcutEditing(ShortcutDTO shortcut) {
        super();

        this.shortcut = shortcut;
    }

    public ShortcutDTO getShortcut() {
        return this.shortcut;
    }
}
