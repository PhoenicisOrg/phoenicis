package com.playonlinux.javafx.views;

import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.javafx.views.common.widget.PlayOnLinuxLogo;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.javafx.views.mainwindow.console.ConsoleTabFactory;
import com.playonlinux.javafx.views.mainwindow.containers.ContainerPanelFactory;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.containers.WinePrefixContainerPanel;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import com.playonlinux.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;
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

    @Value("${application.theme:default}")
    private String themeName;

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Bean
    public ViewApps viewApps() {
        return new ViewApps(themeName);
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
    public PlayOnLinuxLogo playOnLinuxLogo() {
        return new PlayOnLinuxLogo(applicationName);
    }

    @Bean
    public ViewLibrary viewLibrary() {
        return viewsConfigurationLibrary.viewLibrary();
    }

    @Bean
    public ConsoleTabFactory consoleTabFactory() {
        return viewsConfigurationLibrary.consoleTabFactory();
    }

    @Bean
    public ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixDTO> wineContainerPanelFactory() {
        return new ContainerPanelFactory<>(WinePrefixContainerPanel.class, WinePrefixDTO.class);
    }
}
