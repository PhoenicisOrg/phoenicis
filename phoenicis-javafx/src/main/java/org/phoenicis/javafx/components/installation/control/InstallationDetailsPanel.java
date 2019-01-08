package org.phoenicis.javafx.components.installation.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.components.installation.skin.InstallationDetailsPanelSkin;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

/**
 * A details panel for the installations tab used to show the details for a selected installation
 */
public class InstallationDetailsPanel extends DetailsPanelBase<InstallationDetailsPanel, InstallationDetailsPanelSkin> {
    /**
     * The currently shown installation
     */
    private final ObjectProperty<InstallationDTO> installation;

    /**
     * Constructor
     *
     * @param installation The currently shown installation
     * @param onClose The callback for close button clicks
     */
    protected InstallationDetailsPanel(ObjectProperty<InstallationDTO> installation, ObjectProperty<Runnable> onClose) {
        super(onClose);

        this.installation = installation;
    }

    /**
     * Constructor
     */
    public InstallationDetailsPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallationDetailsPanelSkin createSkin() {
        return new InstallationDetailsPanelSkin(this);
    }

    public InstallationDTO getInstallation() {
        return installation.get();
    }

    public ObjectProperty<InstallationDTO> installationProperty() {
        return installation;
    }

    public void setInstallation(InstallationDTO installation) {
        this.installation.set(installation);
    }
}
