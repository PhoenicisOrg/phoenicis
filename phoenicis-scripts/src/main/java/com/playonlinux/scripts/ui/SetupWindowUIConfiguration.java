package com.playonlinux.scripts.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface SetupWindowUIConfiguration {
    @Bean
    SetupWindowFactory setupWindowFactory();

    @Bean
    UIMessageSender uiMessageSender();
}
