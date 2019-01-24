package org.phoenicis.javafx.components.library.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.components.library.skin.ShortcutDetailsPanelSkin;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.function.Consumer;

/**
 * A details panel for the library tab used to show the details for a selected shortcut
 */
public class ShortcutDetailsPanel extends DetailsPanelBase<ShortcutDetailsPanel, ShortcutDetailsPanelSkin> {
    /**
     * The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     */
    private final ObjectMapper objectMapper;

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
     * @param onClose The callback for close button clicks
     * @param onShortcutRun The callback method for when a {@link ShortcutDTO} should be executed
     * @param onShortcutStop The callback method for when a {@link ShortcutDTO} should be stopped
     * @param onShortcutUninstall The callback method for when a {@link ShortcutDTO} should be uninstalled
     */
    public ShortcutDetailsPanel(ObjectMapper objectMapper, ObjectProperty<ShortcutDTO> shortcut,
            ObjectProperty<Runnable> onClose, ObjectProperty<Consumer<ShortcutDTO>> onShortcutRun,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutStop,
            ObjectProperty<Consumer<ShortcutDTO>> onShortcutUninstall) {
        super(onClose);

        this.objectMapper = objectMapper;
        this.shortcut = shortcut;
        this.onShortcutRun = onShortcutRun;
        this.onShortcutStop = onShortcutStop;
        this.onShortcutUninstall = onShortcutUninstall;
    }

    /**
     * Constructor
     *
     * @param objectMapper The {@link ObjectMapper} used to load the properties from a {@link ShortcutDTO}
     * @param shortcut The currently shown {@link ShortcutDTO} object
     */
    public ShortcutDetailsPanel(ObjectMapper objectMapper, ObjectProperty<ShortcutDTO> shortcut) {
        this(objectMapper, shortcut, new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortcutDetailsPanelSkin createSkin() {
        return new ShortcutDetailsPanelSkin(this);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public Consumer<ShortcutDTO> getOnShortcutRun() {
        return onShortcutRun.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutRunProperty() {
        return onShortcutRun;
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.onShortcutRun.set(onShortcutRun);
    }

    public Consumer<ShortcutDTO> getOnShortcutStop() {
        return onShortcutStop.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutStopProperty() {
        return onShortcutStop;
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
