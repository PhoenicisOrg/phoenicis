package com.playonlinux.containers.dto;

import com.phoenicis.entities.Architecture;

public class WinePrefixDTO extends ContainerDTO {
    private final Architecture prefixArchitecture;
    private final String distribution;
    private final String version;

    public WinePrefixDTO(String name, String path, ContainerType type, Architecture prefixArchitecture, String distribution, String version) {
        super(name, path, type);
        this.prefixArchitecture = prefixArchitecture;
        this.distribution = distribution;
        this.version = version;
    }

    public Architecture getPrefixArchitecture() {
        return prefixArchitecture;
    }


    public String getDistribution() {
        return distribution;
    }

    public String getVersion() {
        return version;
    }
}
