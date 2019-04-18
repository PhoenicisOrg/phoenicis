package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerToolsPanelSkin;

/**
 * A component used to execute tools inside an existing container
 */
public class ContainerToolsPanel extends ControlBase<ContainerToolsPanel, ContainerToolsPanelSkin> {
    /**
     * The container
     */
    private final ObjectProperty<ContainerDTO> container;

    /**
     * The container engine controller used to execute a tool inside a container
     */
    private final ObjectProperty<ContainerEngineController> containerEngineController;

    /**
     * A boolean signifying whether all tool buttons should be locked
     */
    private final BooleanProperty lockTools;

    /**
     * Constructor
     */
    public ContainerToolsPanel() {
        super();

        this.container = new SimpleObjectProperty<>();
        this.containerEngineController = new SimpleObjectProperty<>();
        this.lockTools = new SimpleBooleanProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerToolsPanelSkin createSkin() {
        return new ContainerToolsPanelSkin(this);
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

    public ContainerEngineController getContainerEngineController() {
        return this.containerEngineController.get();
    }

    public ObjectProperty<ContainerEngineController> containerEngineControllerProperty() {
        return this.containerEngineController;
    }

    public void setContainerEngineController(ContainerEngineController containerEngineController) {
        this.containerEngineController.set(containerEngineController);
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
