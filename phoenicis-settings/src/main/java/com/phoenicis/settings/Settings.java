package com.phoenicis.settings;

import java.util.Properties;

public class Settings {
    private Properties properties = new Properties();

    public String get(Setting setting) {
        return properties.getProperty(setting.toString());
    }

    public void set(Setting setting, String value) {
        properties.setProperty(setting.toString(), value);
    }

    public Properties getProperties() {
        return properties;
    }
}
