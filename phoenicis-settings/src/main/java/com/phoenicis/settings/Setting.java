package com.phoenicis.settings;

public enum Setting {
    THEME("application.theme"),
    REPOSITORY("application.repository.configuration");

    private final String propertyName;

    Setting(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return this.propertyName;
    }
}
