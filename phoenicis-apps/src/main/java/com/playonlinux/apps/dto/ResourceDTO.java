package com.playonlinux.apps.dto;

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
}
