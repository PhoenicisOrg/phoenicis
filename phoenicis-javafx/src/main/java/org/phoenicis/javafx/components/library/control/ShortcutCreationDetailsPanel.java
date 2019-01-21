package org.phoenicis.javafx.components.library.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.components.library.skin.ShortcutCreationDetailsPanelSkin;
import org.phoenicis.library.dto.ShortcutCreationDTO;

import java.util.function.Consumer;

/**
 * A details panel used to create a new shortcut in the library tab
 */
public class ShortcutCreationDetailsPanel
        extends DetailsPanelBase<ShortcutCreationDetailsPanel, ShortcutCreationDetailsPanelSkin> {
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
     * @param onClose The callback for close button clicks
     */
    public ShortcutCreationDetailsPanel(StringProperty containersPath,
            ObjectProperty<Consumer<ShortcutCreationDTO>> onCreateShortcut, ObjectProperty<Runnable> onClose) {
        super(onClose);

        this.containersPath = containersPath;
        this.onCreateShortcut = onCreateShortcut;
    }

    /**
     * Constructor
     */
    public ShortcutCreationDetailsPanel() {
        this(new SimpleStringProperty(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortcutCreationDetailsPanelSkin createSkin() {
        return new ShortcutCreationDetailsPanelSkin(this);
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
