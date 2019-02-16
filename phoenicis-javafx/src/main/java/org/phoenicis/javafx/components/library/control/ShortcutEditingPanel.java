package org.phoenicis.javafx.components.library.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.library.skin.ShortcutEditingPanelSkin;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.function.Consumer;

/**
 * A details panel component for the library tab used to edit a shortcut
 */
public class ShortcutEditingPanel extends ControlBase<ShortcutEditingPanel, ShortcutEditingPanelSkin> {
    /**
     * The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     */
    private final ObjectProperty<ObjectMapper> objectMapper;

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
     */
    public ShortcutEditingPanel(ObjectProperty<ObjectMapper> objectMapper, ObjectProperty<ShortcutDTO> shortcut,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutChanged) {
        super();

        this.objectMapper = objectMapper;
        this.shortcut = shortcut;
        this.onShortcutChanged = onShortcutChanged;
    }

    /**
     * Constructor
     */
    public ShortcutEditingPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortcutEditingPanelSkin createSkin() {
        return new ShortcutEditingPanelSkin(this);
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper.get();
    }

    public ObjectProperty<ObjectMapper> objectMapperProperty() {
        return this.objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper.set(objectMapper);
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
