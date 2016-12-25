package com.playonlinux.containers.dto;

public class ContainerDTO {
    private final String name;
    private final ContainerType type;

    public ContainerDTO(String name, ContainerType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ContainerType getType() {
        return type;
    }

    public enum ContainerType {
        WINEPREFIX
    }
}
