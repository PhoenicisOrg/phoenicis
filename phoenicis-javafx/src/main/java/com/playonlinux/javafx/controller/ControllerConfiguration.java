package com.playonlinux.javafx.controller;

import com.phoenicis.library.LibraryConfiguration;
import com.phoenicis.settings.SettingsConfiguration;
import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.containers.ContainersConfiguration;
import com.playonlinux.engines.EnginesConfiguration;
import com.playonlinux.javafx.controller.apps.AppsController;
import com.playonlinux.javafx.controller.containers.ContainersController;
import com.playonlinux.javafx.controller.engines.EnginesController;
import com.playonlinux.javafx.controller.library.LibraryController;
import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.controller.settings.SettingsController;
import com.playonlinux.javafx.views.ViewsConfiguration;
import com.playonlinux.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.theme:defaultTheme.css}")
    private String theme;

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

    @Autowired
    private ContainersConfiguration containersConfiguration;

    @Autowired
    private SettingsConfiguration settingsConfiguration;

    @Bean
    public MainController mainController() {
        return new MainController(
                theme,
                applicationName,
                libraryController(),
                appsController(),
                enginesController(),
                containersController(),
                settingsController(),
                viewsConfiguration.playOnLinuxLogo());
    }

    @Bean
    public ContainersController containersController() {
        return new ContainersController(
                viewsConfiguration.viewContainers(),
                containersConfiguration.backgroundContainersManager(),
                viewsConfiguration.wineContainerPanelFactory(),
                containersConfiguration.wineContainerController()
        );
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
                libraryConfiguration.shortcutManager(),
                scriptsConfiguration.scriptInterpreter()
        );
    }

    @Bean
    public AppsController appsController() {
        return new AppsController(
                viewsConfiguration.viewApps(),
                appsConfiguration.backgroundAppsSource(),
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

    @Bean
    public SettingsController settingsController() {
        return new SettingsController(
                viewsConfiguration.viewSettings(),
                settingsConfiguration.settingsSaver()
        );
    }
}
