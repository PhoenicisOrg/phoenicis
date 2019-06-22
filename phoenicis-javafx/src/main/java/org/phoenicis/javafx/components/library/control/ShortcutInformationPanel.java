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
     * The callback method for when a {@link ShortcutDTO} should be edited
     */
    private final ObjectProperty<Consumer<ShortcutDTO>> onShortcutEdit;

    /**
     * The currently shown {@link ShortcutDTO} object
     */
    private final ObjectProperty<ShortcutDTO> shortcut;

    /**
     * Constructor
     */
    public ShortcutInformationPanel() {
        super();

        this.objectMapper = new SimpleObjectProperty<>();
        this.shortcut = new SimpleObjectProperty<>();
        this.onShortcutRun = new SimpleObjectProperty<>();
        this.onShortcutStop = new SimpleObjectProperty<>();
        this.onShortcutUninstall = new SimpleObjectProperty<>();
        this.onShortcutEdit = new SimpleObjectProperty<>();
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
        return this.onShortcutUninstall.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutUninstallProperty() {
        return this.onShortcutUninstall;
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.onShortcutUninstall.set(onShortcutUninstall);
    }

    public Consumer<ShortcutDTO> getOnShortcutEdit() {
        return this.onShortcutEdit.get();
    }

    public ObjectProperty<Consumer<ShortcutDTO>> onShortcutEditProperty() {
        return this.onShortcutEdit;
    }

    public void setOnShortcutEdit(Consumer<ShortcutDTO> onShortcutEdit) {
        this.onShortcutEdit.set(onShortcutEdit);
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
}
