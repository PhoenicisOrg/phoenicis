package com.phoenicis.library;

import com.playonlinux.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryConfiguration {
    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Value("${application.user.shortcuts}")
    private String shortcutDirectory;

    @Bean
    public LibraryManager libraryManager() {
        return new LibraryManager(shortcutDirectory);
    }

    @Bean
    public ShortcutManager shortcutManager() {
        return new ShortcutManager(shortcutDirectory, libraryManager());
    }

    @Bean
    public ShortcutRunner shortcutRunner() {
        return new ShortcutRunner(scriptsConfiguration.scriptInterpreter(), libraryManager());
    }
}
