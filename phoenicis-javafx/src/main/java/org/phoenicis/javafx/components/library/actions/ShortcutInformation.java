package org.phoenicis.javafx.components.library.actions;

import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.library.dto.ShortcutDTO;

/**
 * Indicator to open the "Shortcut Details" details panel
 */
public class ShortcutInformation implements OpenDetailsPanel {
    /**
     * The shortcut whose details are to be shown
     */
    private final ShortcutDTO shortcut;

    public ShortcutInformation(ShortcutDTO shortcut) {
        super();

        this.shortcut = shortcut;
    }

    public ShortcutDTO getShortcut() {
        return this.shortcut;
    }
}
