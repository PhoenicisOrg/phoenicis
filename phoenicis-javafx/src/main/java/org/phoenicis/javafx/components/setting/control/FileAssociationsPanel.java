package org.phoenicis.javafx.components.setting.control;

import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.setting.skin.FileAssociationsPanelSkin;

/**
 * An implementation of the "file associations" panel in the settings tab
 */
public class FileAssociationsPanel extends ControlBase<FileAssociationsPanel, FileAssociationsPanelSkin> {
    /**
     * {@inheritDoc}
     */
    @Override
    public FileAssociationsPanelSkin createSkin() {
        return new FileAssociationsPanelSkin(this);
    }
}
