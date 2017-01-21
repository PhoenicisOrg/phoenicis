package com.phoenicis.settings;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SettingsManager {
    @Value("${application.theme}")
    private String theme;

    @Value("${application.repository.configuration}")
    private String repository;

    private String settingsFileName = "config.properties";

    public SettingsManager(String settingsFileName) {
        this.settingsFileName = settingsFileName;
    }

    public void save(Settings settings) {
        try {
            File file = new File(settingsFileName);
            OutputStream outputStream = new FileOutputStream(file);
            DefaultPropertiesPersister persister = new DefaultPropertiesPersister();
            persister.store(settings.getProperties(), outputStream, "PoL 5 User Settings");
        } catch (Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Save settings");
    }

    public Settings load() {
        Settings settings = new Settings();
        settings.set(Setting.THEME, theme);
        settings.set(Setting.REPOSITORY, repository);
        return settings;
    }
}
