package org.phoenicis.javafx.components.container.control;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerEngineToolsPanelSkin;
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
     *
     * @param container The container
     * @param engineTools The application containing the tool scripts
     * @param engineToolsManager The engine tools manager
     * @param lockTools A boolean signifying whether all tool buttons should be locked
     */
    public ContainerEngineToolsPanel(ObjectProperty<ContainerDTO> container, ObjectProperty<ApplicationDTO> engineTools,
            ObjectProperty<EngineToolsManager> engineToolsManager, BooleanProperty lockTools) {
        super();

        this.container = container;
        this.engineTools = engineTools;
        this.engineToolsManager = engineToolsManager;
        this.lockTools = lockTools;

        this.engineToolScripts = createEngineToolScripts();
    }

    /**
     * Constructor
     */
    public ContainerEngineToolsPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleBooleanProperty());
    }

    /**
     * Creates an {@link ObservableList} containing all tool {@link ScriptDTO}s contained in <code>engineTools</code>
     *
     * @return An {@link ObservableList} containing all tool {@link ScriptDTO}s contained in <code>engineTools</code>
     */
    private ObservableList<ScriptDTO> createEngineToolScripts() {
        final ObservableList<ScriptDTO> engineToolScripts = FXCollections.observableArrayList();

        engineTools.addListener((Observable invalidation) -> {
            final ApplicationDTO engineTools = getEngineTools();

            if (engineTools != null) {
                engineToolScripts.setAll(engineTools.getScripts());
            } else {
                engineToolScripts.clear();
            }
        });

        return engineToolScripts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerEngineToolsPanelSkin createSkin() {
        return new ContainerEngineToolsPanelSkin(this);
    }

    public ContainerDTO getContainer() {
        return container.get();
    }

    public ObjectProperty<ContainerDTO> containerProperty() {
        return container;
    }

    public void setContainer(ContainerDTO container) {
        this.container.set(container);
    }

    public ApplicationDTO getEngineTools() {
        return engineTools.get();
    }

    public ObjectProperty<ApplicationDTO> engineToolsProperty() {
        return engineTools;
    }

    public void setEngineTools(ApplicationDTO engineTools) {
        this.engineTools.set(engineTools);
    }

    public ObservableList<ScriptDTO> getEngineToolScripts() {
        return engineToolScripts;
    }

    public EngineToolsManager getEngineToolsManager() {
        return engineToolsManager.get();
    }

    public ObjectProperty<EngineToolsManager> engineToolsManagerProperty() {
        return engineToolsManager;
    }

    public void setEngineToolsManager(EngineToolsManager engineToolsManager) {
        this.engineToolsManager.set(engineToolsManager);
    }

    public boolean isLockTools() {
        return lockTools.get();
    }

    public BooleanProperty lockToolsProperty() {
        return lockTools;
    }

    public void setLockTools(boolean lockTools) {
        this.lockTools.set(lockTools);
    }
}
