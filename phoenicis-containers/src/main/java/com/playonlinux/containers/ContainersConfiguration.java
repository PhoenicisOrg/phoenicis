package com.playonlinux.containers;

import com.playonlinux.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainersConfiguration {
    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Bean
    public ContainersManager containersManager() {
        return new WinePrefixesManager(toolsConfiguration.compatibleConfigFileFormatFactory());
    }
}
