package com.playonlinux.containers.dto;

public class ContainerDTO {
    private final String name;
    private final String path;
    private final ContainerType type;

    public ContainerDTO(String name, String path, ContainerType type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ContainerType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public enum ContainerType {
        WINEPREFIX
    }
}
