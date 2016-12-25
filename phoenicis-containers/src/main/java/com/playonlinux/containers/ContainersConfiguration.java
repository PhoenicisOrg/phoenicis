package com.playonlinux.containers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainersConfiguration {
    @Bean
    public ContainersManager containersManager() {
        return new WinePrefixesManager();
    }
}
