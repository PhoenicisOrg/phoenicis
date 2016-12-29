package com.playonlinux.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppsConfiguration {
    @Value("${scripts.git.url}")
    private String gitRepositoryDirectory;

    @Value("${application.user.repository}")
    private String localRepositoryDirectory;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Bean
    public ApplicationsSource appsSource() {
        GitApplicationsSource gitAppsSource = new GitApplicationsSource(gitRepositoryDirectory,
                new LocalApplicationsSource.Factory(new ObjectMapper()));
        LocalApplicationsSource localAppsSource = (new LocalApplicationsSource.Factory(new ObjectMapper())).
                createInstance(localRepositoryDirectory);
        return new TeeApplicationsSource(gitAppsSource, localAppsSource);
    }

    @Bean
    public ApplicationsSource backgroundAppsSource() {
        return new BackgroundApplicationsSource(appsSource(), multithreadingConfiguration.appsExecutorService());
    }

}
