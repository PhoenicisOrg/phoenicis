package com.playonlinux.containers;

import com.playonlinux.containers.wine.WineContainerController;
import com.playonlinux.containers.wine.WinePrefixesManager;
import com.playonlinux.containers.wine.configurations.*;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import com.playonlinux.scripts.ScriptsConfiguration;
import com.playonlinux.tools.ToolsConfiguration;
import com.playonlinux.win32.Win32Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainersConfiguration {
    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Autowired
    private Win32Configuration win32Configuration;

    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Value("${application.user.engines.wine}")
    private String wineEnginesPath;

    @Bean
    public ContainersManager containersManager() {
        return new WinePrefixesManager(
                toolsConfiguration.compatibleConfigFileFormatFactory(),
                winePrefixDisplayConfiguration(),
                winePrefixInputConfiguration()
        );
    }

    @Bean
    public ContainersManager backgroundContainersManager() {
        return new BackgroundContainersManager(containersManager(), multithreadingConfiguration.containersExecutorService());
    }

    @Bean
    public WineContainerController wineContainerController() {
        return new WineContainerController(scriptsConfiguration.scriptInterpreter(), toolsConfiguration.terminalOpener(), wineEnginesPath, toolsConfiguration.operatingSystemFetcher());
    }

    @Bean
    WinePrefixInputConfiguration winePrefixInputConfiguration() {
        return new RegistryWinePrefixInputConfiguration(win32Configuration.registryParser(), new DefaultWinePrefixInputConfiguration());
    }

    @Bean
    WinePrefixDisplayConfiguration winePrefixDisplayConfiguration() {
        return new RegistryWinePrefixDisplayConfiguration(win32Configuration.registryParser(), new DefaultWinePrefixDisplayConfiguration());
    }
}
