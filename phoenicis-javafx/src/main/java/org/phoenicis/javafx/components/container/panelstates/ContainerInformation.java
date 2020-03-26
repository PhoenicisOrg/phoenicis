package org.phoenicis.javafx.components.container.panelstates;

import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.components.common.panelstates.OpenDetailsPanel;

/**
 * Indicator to open the "Container Details" details panel
 */
public class ContainerInformation implements OpenDetailsPanel {
    private final ContainerDTO container;

    /**
     * The container whose details are to be shown
     */
    public ContainerInformation(ContainerDTO container) {
        super();

        this.container = container;
    }

    public ContainerDTO getContainer() {
        return this.container;
    }
}
