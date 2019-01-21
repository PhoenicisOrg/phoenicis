package org.phoenicis.javafx.components.engine.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.components.engine.skin.EngineDetailsPanelSkin;

import java.util.function.Consumer;

/**
 * A details panel for the engine tab used to show the details for a selected engine
 */
public class EngineDetailsPanel extends DetailsPanelBase<EngineDetailsPanel, EngineDetailsPanelSkin> {
    /**
     * The {@link Engine} object providing some utility functions to check
     * whether a specific engine version has been installed
     */
    private final ObjectProperty<Engine> engine;

    /**
     * The {@link EngineDTO} object
     */
    private final ObjectProperty<EngineDTO> engineDTO;

    /**
     * The callback for install button clicks
     */
    private final ObjectProperty<Consumer<EngineDTO>> onEngineInstall;

    /**
     * The callback for delete/uninstall button clicks
     */
    private final ObjectProperty<Consumer<EngineDTO>> onEngineDelete;

    /**
     * Constructor
     *
     * @param engine The {@link Engine} object
     * @param engineDTO The {@link EngineDTO} object
     * @param onClose The callback for close button clicks
     * @param onEngineInstall The callback for install button clicks
     * @param onEngineDelete The callback for delete/uninstall button clicks
     */
    protected EngineDetailsPanel(ObjectProperty<Engine> engine, ObjectProperty<EngineDTO> engineDTO,
            ObjectProperty<Runnable> onClose, ObjectProperty<Consumer<EngineDTO>> onEngineInstall,
            ObjectProperty<Consumer<EngineDTO>> onEngineDelete) {
        super(onClose);

        this.engine = engine;
        this.engineDTO = engineDTO;
        this.onEngineInstall = onEngineInstall;
        this.onEngineDelete = onEngineDelete;
    }

    /**
     * Constructor
     */
    public EngineDetailsPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineDetailsPanelSkin createSkin() {
        return new EngineDetailsPanelSkin(this);
    }

    public Engine getEngine() {
        return engine.get();
    }

    public ObjectProperty<Engine> engineProperty() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine.set(engine);
    }

    public EngineDTO getEngineDTO() {
        return engineDTO.get();
    }

    public ObjectProperty<EngineDTO> engineDTOProperty() {
        return engineDTO;
    }

    public void setEngineDTO(EngineDTO engineDTO) {
        this.engineDTO.set(engineDTO);
    }

    public Consumer<EngineDTO> getOnEngineInstall() {
        return onEngineInstall.get();
    }

    public ObjectProperty<Consumer<EngineDTO>> onEngineInstallProperty() {
        return onEngineInstall;
    }

    public void setOnEngineInstall(Consumer<EngineDTO> onEngineInstall) {
        this.onEngineInstall.set(onEngineInstall);
    }

    public Consumer<EngineDTO> getOnEngineDelete() {
        return onEngineDelete.get();
    }

    public ObjectProperty<Consumer<EngineDTO>> onEngineDeleteProperty() {
        return onEngineDelete;
    }

    public void setOnEngineDelete(Consumer<EngineDTO> onEngineDelete) {
        this.onEngineDelete.set(onEngineDelete);
    }
}
