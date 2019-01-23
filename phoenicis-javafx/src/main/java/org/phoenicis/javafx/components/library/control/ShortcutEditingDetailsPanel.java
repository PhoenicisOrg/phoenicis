package org.phoenicis.javafx.components.library.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.components.library.skin.ShortcutEditingDetailsPanelSkin;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.function.Consumer;

/**
 * A details panel component for the library tab used to edit a shortcut
 */
public class ShortcutEditingDetailsPanel
        extends DetailsPanelBase<ShortcutEditingDetailsPanel, ShortcutEditingDetailsPanelSkin> {
    /**
     * The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     */
    private final ObjectMapper objectMapper;

    /**
     * The currently shown {@link ShortcutDTO} object
     */
    private final ObjectProperty<ShortcutDTO> shortcut;

    /**
     * The consumer which is called when a shortcut changed
     */
    private final ObjectProperty<Consumer<ShortcutDTO>> onShortcutChanged;

    /**
     * Constructor
     *
     * @param objectMapper The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     * @param shortcut The currently shown {@link ShortcutDTO} object
     * @param onShortcutChanged The consumer which is called when a shortcut changed
     * @param onClose The callback for close button clicks
     */
    public ShortcutEditingDetailsPanel(ObjectMapper objectMapper, ObjectProperty<ShortcutDTO> shortcut,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutChanged, ObjectProperty<Runnable> onClose) {
        super(onClose);

        this.objectMapper = objectMapper;
        this.shortcut = shortcut;
        this.onShortcutChanged = onShortcutChanged;
    }

    /**
     * Constructor
     *
     * @param objectMapper The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     */
    public ShortcutEditingDetailsPanel(ObjectMapper objectMapper) {
        this(objectMapper, new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortcutEditingDetailsPanelSkin createSkin() {
        return new ShortcutEditingDetailsPanelSkin(this);
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    public ShortcutDTO getShortcut() {
        return this.shortcut.get();
    }

    public ObjectProperty<ShortcutDTO> shortcutProperty() {
        return this.shortcut;
    }

    public void setShortcut(ShortcutDTO shortcut) {
        this.shortcut.set(shortcut);
    }

    public Consumer<ShortcutDTO> getOnShortcutChanged() {
        return this.onShortcutChanged.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutChangedProperty() {
        return this.onShortcutChanged;
    }

    public void setOnShortcutChanged(Consumer<ShortcutDTO> onShortcutChanged) {
        this.onShortcutChanged.set(onShortcutChanged);
    }
}
