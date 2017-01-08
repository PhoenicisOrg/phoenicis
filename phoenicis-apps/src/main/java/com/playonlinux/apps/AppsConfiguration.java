package com.playonlinux.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import com.playonlinux.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppsConfiguration {
    @Value("${application.repository.configuration}")
    private String repositoryConfiguration;

    @Value("${application.repository.forceIncompatibleOperatingSystems:false}")
    private boolean enforceUncompatibleOperatingSystems;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Bean
    public ApplicationsSource appsSource() {
        return new FilterApplicationSource(
                new ConfigurableApplicationSource(
                    repositoryConfiguration,
                    new LocalApplicationsSource.Factory(new ObjectMapper())
                ), toolsConfiguration.operatingSystemFetcher(),
                enforceUncompatibleOperatingSystems);
    }

    @Bean
    public ApplicationsSource backgroundAppsSource() {
        return new BackgroundApplicationsSource(appsSource(), multithreadingConfiguration.appsExecutorService());
    }

}
