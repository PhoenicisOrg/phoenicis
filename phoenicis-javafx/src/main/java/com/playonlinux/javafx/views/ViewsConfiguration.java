package com.playonlinux.javafx.views;

import com.playonlinux.javafx.views.common.widget.PlayOnLinuxLogo;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.javafx.views.mainwindow.console.ConsoleTabFactory;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import com.playonlinux.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;
import com.playonlinux.javafx.views.mainwindow.MainWindowHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ViewsConfigurationLibrary.class)
public class ViewsConfiguration {
    @Value("${application.name}")
    private String applicationName;

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Bean
    public ViewApps viewApps() {
        return new ViewApps();
    }

    @Bean
    public ViewEngines viewEngines() {
        return new ViewEngines();
    }

    @Bean
    public ViewContainers viewContainers() {
        return new ViewContainers();
    }

    @Bean
    public ViewSettings viewSettings() {
        return new ViewSettings();
    }

    @Bean
    PlayOnLinuxLogo playOnLinuxLogo() {
        return new PlayOnLinuxLogo(applicationName);
    }

    @Bean
    public MainWindowHeader mainWindowHeader() {
        return new MainWindowHeader(playOnLinuxLogo());
    }

    @Bean
    public ViewLibrary viewLibrary() {
        return viewsConfigurationLibrary.viewLibrary();
    }

    @Bean
    public ConsoleTabFactory consoleTabFactory() {
        return viewsConfigurationLibrary.consoleTabFactory();
    }
}
