package com.playonlinux.containers.dto;

import com.phoenicis.entities.Architecture;

public class WinePrefixDTO extends ContainerDTO {
    private final Architecture prefixArchitecture;

    public WinePrefixDTO(String name, ContainerType type, Architecture prefixArchitecture) {
        super(name, type);
        this.prefixArchitecture = prefixArchitecture;
    }

    public Architecture getPrefixArchitecture() {
        return prefixArchitecture;
    }
}
