package com.playonlinux.containers.dto;

import com.phoenicis.entities.Architecture;

public class WinePrefixDTO extends ContainerDTO {
    private final String architecture;
    private final String distribution;
    private final String version;

    public WinePrefixDTO(String name, String path, ContainerType type, String architecture, String distribution, String version) {
        super(name, path, type);
        this.architecture = architecture;
        this.distribution = distribution;
        this.version = version;
    }

    public String getArchitecture() {
        return architecture;
    }


    public String getDistribution() {
        return distribution;
    }

    public String getVersion() {
        return version;
    }
}
