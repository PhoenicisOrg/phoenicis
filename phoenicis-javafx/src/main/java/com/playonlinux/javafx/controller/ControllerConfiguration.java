package com.playonlinux.javafx.controller;

import com.phoenicis.library.LibraryConfiguration;
import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.javafx.controller.apps.AppsController;
import com.playonlinux.javafx.controller.library.LibraryController;
import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.views.ViewsConfiguration;
import com.playonlinux.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {
    @Autowired
    private ViewsConfiguration viewsConfiguration;

    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Autowired
    private AppsConfiguration appsConfiguration;

    @Autowired
    private LibraryConfiguration libraryConfiguration;

    @Bean
    public MainController mainController() {
        return new MainController(
                libraryController(),
                appsController(),
                viewsConfiguration.viewEngines(),
                viewsConfiguration.viewContainers(),
                viewsConfiguration.viewSettings(),
                viewsConfiguration.mainWindowHeader());
    }

    @Bean
    public LibraryController libraryController() {
        return new LibraryController(viewsConfiguration.viewLibrary(), consoleController(), libraryConfiguration.libraryManager());
    }

    @Bean
    public AppsController appsController() {
        return new AppsController(
                viewsConfiguration.viewApps(),
                appsConfiguration.appsManager(),
                scriptsConfiguration.scriptInterpreter()
        );
    }

    @Bean
    public ConsoleController consoleController() {
        return new ConsoleController(
                viewsConfiguration.consoleTabFactory(),
                scriptsConfiguration.scriptInterpreter()
        );
    }
}
