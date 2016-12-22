package com.playonlinux.engines;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnginesConfiguration {
    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Bean
    public WineVersionsManager wineVersionsFetcher() {
        return new WineVersionsManager(scriptsConfiguration.scriptInterpreter(), new ObjectMapper());
    }
}
