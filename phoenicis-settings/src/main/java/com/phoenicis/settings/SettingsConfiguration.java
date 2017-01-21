package com.phoenicis.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettingsConfiguration {

    @Value("${application.user.settings}")
    private String settingsFileName;

    @Bean
    public SettingsManager settingsManager() {
        return new SettingsManager(settingsFileName);
    }
}
