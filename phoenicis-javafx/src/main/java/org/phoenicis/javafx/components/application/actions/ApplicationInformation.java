package org.phoenicis.javafx.components.application.actions;

import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.repository.dto.ApplicationDTO;

/**
 * Indicator to open the "Application Information" details panel
 */
public class ApplicationInformation implements OpenDetailsPanel {
    /**
     * The application whose details are to be shown
     */
    private final ApplicationDTO application;

    public ApplicationInformation(ApplicationDTO application) {
        super();

        this.application = application;
    }

    public ApplicationDTO getApplication() {
        return this.application;
    }
}
