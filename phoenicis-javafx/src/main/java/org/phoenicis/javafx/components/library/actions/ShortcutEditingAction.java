package org.phoenicis.javafx.components.library.actions;

import org.phoenicis.javafx.components.common.actions.DetailsPanelAction;
import org.phoenicis.library.dto.ShortcutDTO;

/**
 * Action to open the "Shortcut Editing" details panel
 */
public class ShortcutEditingAction implements DetailsPanelAction {
    /**
     * The shortcut whose details are to be edited
     */
    private final ShortcutDTO shortcut;

    public ShortcutEditingAction(ShortcutDTO shortcut) {
        super();

        this.shortcut = shortcut;
    }

    public ShortcutDTO getShortcut() {
        return this.shortcut;
    }
}
