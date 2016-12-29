package com.playonlinux.apps.dto;

import java.util.Comparator;

public class ResourceDTO {
    private final String name;
    private final byte[] content;

    public ResourceDTO(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    public static Comparator<ResourceDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }
}
