package org.phoenicis.javafx.components.library.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.library.skin.ShortcutCreationPanelSkin;
import org.phoenicis.library.dto.ShortcutCreationDTO;

import java.util.function.Consumer;

/**
 * A details panel used to create a new shortcut in the library tab
 */
public class ShortcutCreationPanel
        extends ControlBase<ShortcutCreationPanel, ShortcutCreationPanelSkin> {
    /**
     * The path leading to the containers directory
     */
    private final StringProperty containersPath;

    /**
     * The callback for when a shortcut shall be created
     */
    private final ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcut;

    /**
     * Constructor
     *
     * @param containersPath The path leading to the containers directory
     * @param onCreateShortcut The callback for when a shortcut shall be created
     */
    private ShortcutCreationPanel(StringProperty containersPath,
            ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcut) {
        super();

        this.containersPath = containersPath;
        this.onCreateShortcut = onCreateShortcut;
    }

    /**
     * Constructor
     */
    public ShortcutCreationPanel() {
        this(new SimpleStringProperty(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortcutCreationPanelSkin createSkin() {
        return new ShortcutCreationPanelSkin(this);
    }

    public String getContainersPath() {
        return this.containersPath.get();
    }

    public StringProperty containersPathProperty() {
        return this.containersPath;
    }

    public void setContainersPath(String containersPath) {
        this.containersPath.set(containersPath);
    }

    public Consumer<ShortcutCreationDTO> getOnCreateShortcut() {
        return this.onCreateShortcut.get();
    }

    public ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcutProperty() {
        return this.onCreateShortcut;
    }

    public void setOnCreateShortcut(Consumer<ShortcutCreationDTO> onCreateShortcut) {
        this.onCreateShortcut.set(onCreateShortcut);
    }
}
