package org.phoenicis.javafx.components.library.actions;

import org.phoenicis.javafx.components.common.actions.DetailsPanelAction;
import org.phoenicis.library.dto.ShortcutDTO;

/**
 * Action to open the "Shortcut Details" details panel
 */
public class ShortcutDetailsAction implements DetailsPanelAction {
    /**
     * The shortcut whose details are to be shown
     */
    private final ShortcutDTO shortcut;

    public ShortcutDetailsAction(ShortcutDTO shortcut) {
        super();

        this.shortcut = shortcut;
    }

    public ShortcutDTO getShortcut() {
        return this.shortcut;
    }
}
