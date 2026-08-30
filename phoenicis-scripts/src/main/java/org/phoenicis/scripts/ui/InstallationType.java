package org.phoenicis.scripts.ui;

/**
 * type of the installation
 */
public enum InstallationType {
    APPS("Apps"), ENGINES("Engines"), VERBS("Verbs");

    private final String displayName;

    InstallationType(String displayName) {
        this.displayName = displayName;
    }

    public String toString() {
        return this.displayName;
    }
}