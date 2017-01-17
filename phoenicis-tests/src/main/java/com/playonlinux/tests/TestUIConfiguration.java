package com.playonlinux.tests;

import com.playonlinux.cli.setupwindow.CLIMessageSender;
import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.SetupWindowUIConfiguration;
import com.playonlinux.scripts.ui.UIMessageSender;
import com.playonlinux.scripts.ui.UIQuestionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TestUIConfiguration implements SetupWindowUIConfiguration {
    @Override
    @Bean
    public SetupWindowFactory setupWindowFactory() {
        return title -> new TestSetupWindow();
    }

    @Override
    @Bean
    public UIMessageSender uiMessageSender() {
        return new CLIMessageSender();
    }

    @Override
    @Bean
    public UIQuestionFactory uiQuestionFactory() {
        return (questionText, yesCallback, noCallback) -> yesCallback.run();
    }
}
