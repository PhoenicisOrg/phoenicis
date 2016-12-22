package com.playonlinux.scripts.wizard;

import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.UIMessageSender;
import org.springframework.beans.factory.annotation.Value;

public class SetupWizardFactory {
    @Value("${application.user.root}")
    private String homePath;

    private final UIMessageSender uiMessageSender;
    private final SetupWindowFactory setupWindowFactory;

    public SetupWizardFactory(UIMessageSender uiMessageSender, SetupWindowFactory setupWindowFactory) {
        this.uiMessageSender = uiMessageSender;
        this.setupWindowFactory = setupWindowFactory;
    }

    public SetupWizard create(String title) {
        final SetupWizard setupWizard = new SetupWizard(title, uiMessageSender, setupWindowFactory, homePath);
        setupWizard.init();
        return setupWizard;
    }
}
