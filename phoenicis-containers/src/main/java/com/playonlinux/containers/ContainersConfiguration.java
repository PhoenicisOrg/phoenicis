package com.playonlinux.containers;

import com.playonlinux.containers.wine.WinePrefixesManager;
import com.playonlinux.containers.wine.configurations.*;
import com.playonlinux.tools.ToolsConfiguration;
import com.playonlinux.win32.Win32Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainersConfiguration {
    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Autowired
    private Win32Configuration win32Configuration;

    @Bean
    public ContainersManager containersManager() {
        return new WinePrefixesManager(
                toolsConfiguration.compatibleConfigFileFormatFactory(),
                winePrefixDisplayConfiguration(),
                winePrefixInputConfiguration()
        );
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
