package com.playonlinux.scripts.wizard;

import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.UIMessageSender;
import org.springframework.beans.factory.annotation.Value;

public class SetupWizardFactory {
    @Value("${user.home}")
    private String userHome;

    @Value("${application.user.root}")
    private String applicationUserRoot;

    @Value("${application.name}")
    private String applicationName;


    private final UIMessageSender uiMessageSender;
    private final SetupWindowFactory setupWindowFactory;

    public SetupWizardFactory(UIMessageSender uiMessageSender, SetupWindowFactory setupWindowFactory) {
        this.uiMessageSender = uiMessageSender;
        this.setupWindowFactory = setupWindowFactory;
    }

    public SetupWizard create(String title) {
        final SetupWizard setupWizard = new SetupWizard(title, uiMessageSender, setupWindowFactory, userHome, applicationUserRoot, applicationName);
        setupWizard.init();
        return setupWizard;
    }
}
