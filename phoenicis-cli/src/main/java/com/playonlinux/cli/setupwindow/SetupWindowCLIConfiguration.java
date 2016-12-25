package com.playonlinux.cli.setupwindow;

import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.SetupWindowUIConfiguration;
import com.playonlinux.scripts.ui.UIMessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SetupWindowCLIConfiguration implements SetupWindowUIConfiguration {
    @Override
    @Bean
    public SetupWindowFactory setupWindowFactory() {
        return (title) -> new SetupWindowCLIImplementation(title, true, true);
    }

    @Override
    @Bean
    public UIMessageSender uiMessageSender() {
        return new CLIMessageSender();
    }
}
