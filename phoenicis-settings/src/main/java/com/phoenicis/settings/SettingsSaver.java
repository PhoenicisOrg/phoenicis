package com.phoenicis.settings;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SettingsSaver {
    @Value("${application.theme}")
    private String theme;

    @Value("${application.repository.configuration}")
    private String repository;

    private String settingsFileName = "config.properties";

    public SettingsSaver(String settingsFileName) {
        this.settingsFileName = settingsFileName;
    }

    public void save(Properties settings) {
        try {
            File file = new File(settingsFileName);
            OutputStream outputStream = new FileOutputStream(file);
            DefaultPropertiesPersister persister = new DefaultPropertiesPersister();
            persister.store(settings, outputStream, "PoL 5 User Settings");
        } catch (Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Save settings");
    }

    public Properties load() {
        Properties properties = new Properties();
        properties.setProperty("application.theme", theme);
        properties.setProperty("application.repository.configuration",repository);
        return properties;
    }
}
