package org.phoenicis.javafx.components.application.actions;

import org.phoenicis.javafx.components.common.actions.DetailsPanelAction;
import org.phoenicis.repository.dto.ApplicationDTO;

public class ApplicationInformationAction implements DetailsPanelAction {
    private final ApplicationDTO application;

    public ApplicationInformationAction(ApplicationDTO application) {
        super();

        this.application = application;
    }

    public ApplicationDTO getApplication() {
        return this.application;
    }
}
