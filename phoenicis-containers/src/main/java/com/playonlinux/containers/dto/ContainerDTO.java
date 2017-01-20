package com.playonlinux.containers.dto;

import java.util.Comparator;

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

    public static Comparator<ContainerDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    public enum ContainerType {
        WINEPREFIX
    }
}
