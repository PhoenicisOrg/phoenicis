package org.phoenicis.javafx.components.container.control;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerVerbsPanelSkin;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

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
     * A list of all verbs
     */
    private final ObservableList<ScriptDTO> verbScripts;

    /**
     * A boolean signifying whether all verb buttons should be locked
     */
    private final BooleanProperty lockVerbs;

    /**
     * Constructor
     *
     * @param container The container
     * @param verbsManager The verbs manager
     * @param verbs The application containing the verbs
     * @param lockVerbs A boolean signifying whether all verb buttons should be locked
     */
    public ContainerVerbsPanel(ObjectProperty<ContainerDTO> container, ObjectProperty<VerbsManager> verbsManager,
            ObjectProperty<ApplicationDTO> verbs, BooleanProperty lockVerbs) {
        super();

        this.container = container;
        this.verbsManager = verbsManager;
        this.verbs = verbs;
        this.lockVerbs = lockVerbs;

        this.verbScripts = createVerbScripts();
    }

    /**
     * Constructor
     */
    public ContainerVerbsPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleBooleanProperty());
    }

    /**
     * Creates an {@link ObservableList} containing all tool {@link ScriptDTO}s contained in <code>engineTools</code>
     *
     * @return An {@link ObservableList} containing all tool {@link ScriptDTO}s contained in <code>engineTools</code>
     */
    private ObservableList<ScriptDTO> createVerbScripts() {
        final ObservableList<ScriptDTO> verbScripts = FXCollections.observableArrayList();

        verbs.addListener((Observable invalidation) -> {
            final ApplicationDTO verbs = getVerbs();

            if (verbs != null) {
                verbScripts.setAll(verbs.getScripts());
            } else {
                verbScripts.clear();
            }
        });

        return verbScripts;
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
