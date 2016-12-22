package com.playonlinux.tools.system;

import com.phoenicis.entities.Architecture;
import com.phoenicis.entities.OperatingSystem;

public class ArchitectureFetcher {
    private final OperatingSystemFetcher operatingSystemFetcher;

    public ArchitectureFetcher(OperatingSystemFetcher operatingSystemFetcher) {
        this.operatingSystemFetcher = operatingSystemFetcher;
    }

    /**
     * Find the current architecture
     * @return The current architecture
     */
    public Architecture fetchCurrentArchitecture() {
        if(operatingSystemFetcher.fetchCurrentOperationSystem() == OperatingSystem.MACOSX) {
            return Architecture.I386;
        }
        if("x86".equals(System.getProperty("os.arch"))) {
            return Architecture.I386;
        } else {
            return Architecture.AMD64;
        }
    }

    public Architecture fromWinePackageName(String architectureName) {
        switch (architectureName) {
            case "x86":
                return Architecture.I386;
            case "amd64":
                return Architecture.AMD64;
            default:
                throw new IllegalArgumentException(String.format("Unknown architecture '%s'", architectureName));
        }
    }

}
