package org.phoenicis.javafx.components.library.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.components.library.skin.ShortcutCreationDetailsPanelSkin;
import org.phoenicis.library.dto.ShortcutCreationDTO;

import java.util.function.Consumer;

public class ShortcutCreationDetailsPanel
        extends DetailsPanelBase<ShortcutCreationDetailsPanel, ShortcutCreationDetailsPanelSkin> {
    private final StringProperty containersPath;

    /**
     * Consumer called when a shortcut shall be created
     */
    private final ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcut;

    public ShortcutCreationDetailsPanel(StringProperty containersPath,
            ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcut, ObjectProperty<Runnable> onClose) {
        super(onClose);

        this.containersPath = containersPath;
        this.onCreateShortcut = onCreateShortcut;
    }

    public ShortcutCreationDetailsPanel() {
        this(new SimpleStringProperty(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    @Override
    public ShortcutCreationDetailsPanelSkin createSkin() {
        return new ShortcutCreationDetailsPanelSkin(this);
    }

    public String getContainersPath() {
        return containersPath.get();
    }

    public StringProperty containersPathProperty() {
        return containersPath;
    }

    public void setContainersPath(String containersPath) {
        this.containersPath.set(containersPath);
    }

    public Consumer<ShortcutCreationDTO> getOnCreateShortcut() {
        return onCreateShortcut.get();
    }

    public ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcutProperty() {
        return onCreateShortcut;
    }

    public void setOnCreateShortcut(Consumer<ShortcutCreationDTO> onCreateShortcut) {
        this.onCreateShortcut.set(onCreateShortcut);
    }
}
