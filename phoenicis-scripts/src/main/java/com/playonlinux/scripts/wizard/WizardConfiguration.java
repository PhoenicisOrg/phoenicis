package com.playonlinux.scripts.wizard;

import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.SetupWindowUIConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WizardConfiguration {
    @Autowired
    SetupWindowUIConfiguration setupWindowUIConfiguration;

    @Bean
    public SetupWizardFactory setupWindowFactory() {
        return new SetupWizardFactory(setupWindowUIConfiguration.uiMessageSender(), setupWindowUIConfiguration.setupWindowFactory());
    }
}
