package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerEngineToolsPanelSkin;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

/**
 * A component used to show all engine tools for a container
 */
public class ContainerEngineToolsPanel extends ControlBase<ContainerEngineToolsPanel, ContainerEngineToolsPanelSkin> {
    /**
     * The container
     */
    private final ObjectProperty<ContainerDTO> container;

    /**
     * The application containing the tool scripts
     */
    private final ObjectProperty<ApplicationDTO> engineTools;

    /**
     * A list of all tool scripts
     */
    private final ObservableList<ScriptDTO> engineToolScripts;

    /**
     * The engine tools manager
     */
    private final ObjectProperty<EngineToolsManager> engineToolsManager;

    /**
     * A boolean signifying whether all tool buttons should be locked
     */
    private final BooleanProperty lockTools;

    /**
     * Constructor
     */
    public ContainerEngineToolsPanel() {
        super();

        this.container = new SimpleObjectProperty<>();
        this.engineTools = new SimpleObjectProperty<>();
        this.engineToolsManager = new SimpleObjectProperty<>();
        this.lockTools = new SimpleBooleanProperty();

        this.engineToolScripts = CollectionBindings.mapToList(engineToolsProperty(), ApplicationDTO::getScripts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerEngineToolsPanelSkin createSkin() {
        return new ContainerEngineToolsPanelSkin(this);
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

    public ApplicationDTO getEngineTools() {
        return this.engineTools.get();
    }

    public ObjectProperty<ApplicationDTO> engineToolsProperty() {
        return this.engineTools;
    }

    public void setEngineTools(ApplicationDTO engineTools) {
        this.engineTools.set(engineTools);
    }

    public ObservableList<ScriptDTO> getEngineToolScripts() {
        return this.engineToolScripts;
    }

    public EngineToolsManager getEngineToolsManager() {
        return this.engineToolsManager.get();
    }

    public ObjectProperty<EngineToolsManager> engineToolsManagerProperty() {
        return this.engineToolsManager;
    }

    public void setEngineToolsManager(EngineToolsManager engineToolsManager) {
        this.engineToolsManager.set(engineToolsManager);
    }

    public boolean isLockTools() {
        return this.lockTools.get();
    }

    public BooleanProperty lockToolsProperty() {
        return this.lockTools;
    }

    public void setLockTools(boolean lockTools) {
        this.lockTools.set(lockTools);
    }
}
