package org.phoenicis.javafx.components.installation.panelstates;

import org.phoenicis.javafx.components.common.panelstates.OpenDetailsPanel;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

/**
 * Indicator to open the "Installation" details panel
 */
public class Installation implements OpenDetailsPanel {
    /**
     * The installation whose content is to be shown
     */
    private final InstallationDTO installation;

    public Installation(InstallationDTO installation) {
        super();

        this.installation = installation;
    }

    public InstallationDTO getInstallation() {
        return this.installation;
    }
}
