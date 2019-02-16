package org.phoenicis.javafx.components.library.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.library.skin.ShortcutInformationPanelSkin;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.function.Consumer;

/**
 * A details panel for the library tab used to show the details for a selected shortcut
 */
public class ShortcutInformationPanel extends ControlBase<ShortcutInformationPanel, ShortcutInformationPanelSkin> {
    /**
     * The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     */
    private final ObjectProperty<ObjectMapper> objectMapper;

    /**
     * The callback method for when a {@link ShortcutDTO} should be executed
     */
    private final ObjectProperty<Consumer<ShortcutDTO>> onShortcutRun;

    /**
     * The callback method for when a {@link ShortcutDTO} should be stopped
     */
    private final ObjectProperty<Consumer<ShortcutDTO>> onShortcutStop;

    /**
     * The callback method for when a {@link ShortcutDTO} should be uninstalled
     */
    private final ObjectProperty<Consumer<ShortcutDTO>> onShortcutUninstall;

    /**
     * The currently shown {@link ShortcutDTO} object
     */
    private final ObjectProperty<ShortcutDTO> shortcut;

    /**
     * Constructor
     *
     * @param objectMapper The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     * @param shortcut The currently shown {@link ShortcutDTO} object
     * @param onShortcutRun The callback method for when a {@link ShortcutDTO} should be executed
     * @param onShortcutStop The callback method for when a {@link ShortcutDTO} should be stopped
     * @param onShortcutUninstall The callback method for when a {@link ShortcutDTO} should be uninstalled
     */
    public ShortcutInformationPanel(ObjectProperty<ObjectMapper> objectMapper, ObjectProperty<ShortcutDTO> shortcut,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutRun,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutStop,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutUninstall) {
        super();

        this.objectMapper = objectMapper;
        this.shortcut = shortcut;
        this.onShortcutRun = onShortcutRun;
        this.onShortcutStop = onShortcutStop;
        this.onShortcutUninstall = onShortcutUninstall;
    }

    /**
     * Constructor
     */
    public ShortcutInformationPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortcutInformationPanelSkin createSkin() {
        return new ShortcutInformationPanelSkin(this);
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

    public Consumer<ShortcutDTO> getOnShortcutRun() {
        return this.onShortcutRun.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutRunProperty() {
        return this.onShortcutRun;
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.onShortcutRun.set(onShortcutRun);
    }

    public Consumer<ShortcutDTO> getOnShortcutStop() {
        return this.onShortcutStop.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutStopProperty() {
        return this.onShortcutStop;
    }

    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.onShortcutStop.set(onShortcutStop);
    }

    public Consumer<ShortcutDTO> getOnShortcutUninstall() {
        return onShortcutUninstall.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutUninstallProperty() {
        return onShortcutUninstall;
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.onShortcutUninstall.set(onShortcutUninstall);
    }

    public ShortcutDTO getShortcut() {
        return shortcut.get();
    }

    public ObjectProperty<ShortcutDTO> shortcutProperty() {
        return shortcut;
    }

    public void setShortcut(ShortcutDTO shortcut) {
        this.shortcut.set(shortcut);
    }
}
