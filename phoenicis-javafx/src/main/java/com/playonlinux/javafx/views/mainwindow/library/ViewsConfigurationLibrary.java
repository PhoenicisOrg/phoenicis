package com.playonlinux.javafx.views.mainwindow.library;

import com.playonlinux.javafx.views.mainwindow.console.ConsoleTabFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewsConfigurationLibrary {
    @Value("${application.name}")
    private String applicationName;

    @Bean
    public ViewLibrary viewLibrary() {
        return new ViewLibrary(applicationName);
    }

    @Bean
    public ConsoleTabFactory consoleTabFactory() {
        return new ConsoleTabFactory();
    }
}
