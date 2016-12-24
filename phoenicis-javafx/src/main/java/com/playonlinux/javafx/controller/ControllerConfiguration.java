package com.playonlinux.javafx.controller;

import com.phoenicis.library.LibraryConfiguration;
import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.engines.EnginesConfiguration;
import com.playonlinux.javafx.controller.apps.AppsController;
import com.playonlinux.javafx.controller.engines.EnginesController;
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

    @Autowired
    private EnginesConfiguration enginesConfiguration;

    @Bean
    public MainController mainController() {
        return new MainController(
                libraryController(),
                appsController(),
                enginesController(),
                viewsConfiguration.viewContainers(),
                viewsConfiguration.viewSettings(),
                viewsConfiguration.mainWindowHeader());
    }

    @Bean
    public EnginesController enginesController() {
        return new EnginesController(viewsConfiguration.viewEngines(), enginesConfiguration.wineVersionsFetcher());
    }

    @Bean
    public LibraryController libraryController() {
        return new LibraryController(
                viewsConfiguration.viewLibrary(),
                consoleController(),
                libraryConfiguration.libraryManager(),
                libraryConfiguration.shortcutRunner(),
                libraryConfiguration.shortcutManager()
        );
    }

    @Bean
    public AppsController appsController() {
        return new AppsController(
                viewsConfiguration.viewApps(),
                appsConfiguration.backgroundAppsManager(),
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
