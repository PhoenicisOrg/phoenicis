package org.phoenicis.scripts.ui;

import org.phoenicis.configuration.security.Safe;

/**
 * type of the installation
 */
@Safe
public enum InstallationType {
    APPS("Apps"), ENGINES("Engines"), VERBS("Verbs");

    private String displayName;

    InstallationType(String displayName) {
        this.displayName = displayName;
    }

    public String toString() {
        return this.displayName;
    }
}