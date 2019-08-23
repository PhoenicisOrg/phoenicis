package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerVerbsPanelSkin;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.Comparator;

/**
 * A component used to install verbs in an existing container
 */
public class ContainerVerbsPanel extends ControlBase<ContainerVerbsPanel, ContainerVerbsPanelSkin> {
    /**
     * The container
     */
    private final ObjectProperty<ContainerDTO> container;

    /**
     * The verbs manager
     */
    private final ObjectProperty<VerbsManager> verbsManager;

    /**
     * The application containing the verbs
     */
    private final ObjectProperty<ApplicationDTO> verbs;

    /**
     * A list of all verbs sorted according to their names
     */
    private final ObservableList<ScriptDTO> verbScripts;

    /**
     * A boolean signifying whether all verb buttons should be locked
     */
    private final BooleanProperty lockVerbs;

    /**
     * Constructor
     */
    public ContainerVerbsPanel() {
        super();

        this.container = new SimpleObjectProperty<>();
        this.verbsManager = new SimpleObjectProperty<>();
        this.verbs = new SimpleObjectProperty<>();
        this.lockVerbs = new SimpleBooleanProperty();

        this.verbScripts = CollectionBindings
                .mapToList(verbsProperty(), ApplicationDTO::getScripts)
                .sorted(Comparator.comparing(ScriptDTO::getScriptName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerVerbsPanelSkin createSkin() {
        return new ContainerVerbsPanelSkin(this);
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

    public ObservableList<ScriptDTO> getVerbScripts() {
        return this.verbScripts;
    }

    public boolean isLockVerbs() {
        return this.lockVerbs.get();
    }

    public BooleanProperty lockVerbsProperty() {
        return this.lockVerbs;
    }

    public void setLockVerbs(boolean lockVerbs) {
        this.lockVerbs.set(lockVerbs);
    }
}
