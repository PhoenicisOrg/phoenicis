package com.playonlinux.tools.system;

import com.phoenicis.entities.OperatingSystem;

public class OperatingSystemFetcher {
    /**
     * Fetch the current Operating System
     *
     * @return The current operating system
     */
    public OperatingSystem fetchCurrentOperationSystem() {
        return OperatingSystem.fromString(System.getProperty("os.name"));
    }
}
