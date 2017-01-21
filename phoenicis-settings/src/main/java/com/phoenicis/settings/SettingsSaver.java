package com.phoenicis.settings;


import java.util.Properties;

public class SettingsSaver {
    private String settingsFileName = "";

    public SettingsSaver(String settingsFileName) {
        this.settingsFileName = settingsFileName;
    }

    public void save(Properties settings) {
        System.out.println("Save settings");
    }

}
