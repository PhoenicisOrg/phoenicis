package com.playonlinux.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppsConfiguration {
    @Value("${scripts.git.url}")
    private String repositoryDirectory;

    @Bean
    public AppsManager appsManager() {
        return new GitAppsManager(repositoryDirectory, new LocalAppsManager.Factory(new ObjectMapper()));
    }
}
