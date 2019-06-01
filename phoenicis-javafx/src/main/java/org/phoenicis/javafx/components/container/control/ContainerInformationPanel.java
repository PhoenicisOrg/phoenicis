package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerInformationPanelSkin;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.function.Consumer;

/**
 * A component used to show all information for a container.
 * This information consists of:
 * <ul>
 * <li>an overview</li>
 * <li>the engine settings</li>
 * <li>the engine tools</li>
 * <li>the verbs</li>
 * <li>the tools</li>
 * </ul>
 */
public class ContainerInformationPanel extends ControlBase<ContainerInformationPanel, ContainerInformationPanelSkin> {
    /**
     * The shown container
     */
    private final ObjectProperty<ContainerDTO> container;

    /**
     * The container engine controller
     */
    private final ObjectProperty<ContainerEngineController> containerEngineController;

    /**
     * The engine settings
     */
    private final ObservableList<EngineSetting> engineSettings;

    /**
     * The verbs manager
     */
    private final ObjectProperty<VerbsManager> verbsManager;

    /**
     * The {@link ApplicationDTO} containing the verbs
     */
    private final ObjectProperty<ApplicationDTO> verbs;

    /**
     * The engine tools manager
     */
    private final ObjectProperty<EngineToolsManager> engineToolsManager;

    /**
     * The {@link ApplicationDTO} containing the engine tools
     */
    private final ObjectProperty<ApplicationDTO> engineTools;

    /**
     * Callback method for when the container should be deleted
     */
    private final ObjectProperty<Consumer<ContainerDTO>> onDeleteContainer;

    /**
     * Callback method for when the folder containing the container should be openend in a file browser
     */
    private final ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowser;

    /**
     * Constructor
     */
    public ContainerInformationPanel() {
        super();

        this.container = new SimpleObjectProperty<>();
        this.containerEngineController = new SimpleObjectProperty<>();
        this.engineSettings = FXCollections.observableArrayList();
        this.verbsManager = new SimpleObjectProperty<>();
        this.verbs = new SimpleObjectProperty<>();
        this.engineToolsManager = new SimpleObjectProperty<>();
        this.engineTools = new SimpleObjectProperty<>();
        this.onDeleteContainer = new SimpleObjectProperty<>();
        this.onOpenFileBrowser = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerInformationPanelSkin createSkin() {
        return new ContainerInformationPanelSkin(this);
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

    public ObservableList<EngineSetting> getEngineSettings() {
        return this.engineSettings;
    }

    public VerbsManager getVerbsManager() {
        return this.verbsManager.get();
    }

    public ObjectProperty<VerbsManager> verbsManagerProperty() {
        return this.verbsManager;
    }

    public void setVerbsManager(VerbsManager verbsManager) {
        this.verbsManager.set(verbsManager);
    }

    public ApplicationDTO getVerbs() {
        return this.verbs.get();
    }

    public ObjectProperty<ApplicationDTO> verbsProperty() {
        return this.verbs;
    }

    public void setVerbs(ApplicationDTO verbs) {
        this.verbs.set(verbs);
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

    public ApplicationDTO getEngineTools() {
        return this.engineTools.get();
    }

    public ObjectProperty<ApplicationDTO> engineToolsProperty() {
        return this.engineTools;
    }

    public void setEngineTools(ApplicationDTO engineTools) {
        this.engineTools.set(engineTools);
    }

    public Consumer<ContainerDTO> getOnDeleteContainer() {
        return this.onDeleteContainer.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onDeleteContainerProperty() {
        return this.onDeleteContainer;
    }

    public void setOnDeleteContainer(Consumer<ContainerDTO> onDeleteContainer) {
        this.onDeleteContainer.set(onDeleteContainer);
    }

    public Consumer<ContainerDTO> getOnOpenFileBrowser() {
        return this.onOpenFileBrowser.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowserProperty() {
        return this.onOpenFileBrowser;
    }

    public void setOnOpenFileBrowser(Consumer<ContainerDTO> onOpenFileBrowser) {
        this.onOpenFileBrowser.set(onOpenFileBrowser);
    }
}
