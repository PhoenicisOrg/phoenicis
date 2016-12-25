package com.playonlinux.containers.dto;

import com.phoenicis.entities.Architecture;

public class WinePrefixDTO extends ContainerDTO {
    private final Architecture prefixArchitecture;

    public WinePrefixDTO(String name, String path, ContainerType type, Architecture prefixArchitecture) {
        super(name, path, type);
        this.prefixArchitecture = prefixArchitecture;
    }

    public Architecture getPrefixArchitecture() {
        return prefixArchitecture;
    }


}
