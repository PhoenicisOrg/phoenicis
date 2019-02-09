package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerEngineSettingsPanelSkin;

/**
 * A component used to change the engine settings for a container
 */
public class ContainerEngineSettingsPanel
        extends ControlBase<ContainerEngineSettingsPanel, ContainerEngineSettingsPanelSkin> {
    /**
     * The container
     */
    private final ObjectProperty<ContainerDTO> container;

    /**
     * A list of all engine settings
     */
    private final ObservableList<EngineSetting> engineSettings;

    /**
     * A boolean signifying whether all engine settings buttons should be locked
     */
    private final BooleanProperty lockEngineSettings;

    /**
     * Constructor
     *
     * @param container The container
     * @param engineSettings A list of all engine settings
     * @param lockEngineSettings A boolean signifying whether all engine settings buttons should be locked
     */
    public ContainerEngineSettingsPanel(ObjectProperty<ContainerDTO> container,
            ObservableList<EngineSetting> engineSettings, BooleanProperty lockEngineSettings) {
        super();

        this.container = container;
        this.engineSettings = engineSettings;
        this.lockEngineSettings = lockEngineSettings;
    }

    /**
     * Constructor
     */
    public ContainerEngineSettingsPanel() {
        this(new SimpleObjectProperty<>(), FXCollections.observableArrayList(), new SimpleBooleanProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerEngineSettingsPanelSkin createSkin() {
        return new ContainerEngineSettingsPanelSkin(this);
    }

    public ContainerDTO getContainer() {
        return this.container.get();
    }

    public ObjectProperty<ContainerDTO> containerProperty() {
        return this.container;
    }

    public void setContainer(ContainerDTO container) {
        this.container.set(container);
    }

    public ObservableList<EngineSetting> getEngineSettings() {
        return this.engineSettings;
    }

    public boolean isLockEngineSettings() {
        return this.lockEngineSettings.get();
    }

    public BooleanProperty lockEngineSettingsProperty() {
        return this.lockEngineSettings;
    }

    public void setLockEngineSettings(boolean lockEngineSettings) {
        this.lockEngineSettings.set(lockEngineSettings);
    }
}
