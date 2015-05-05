package com.playonlinux.ui.dtos;

import java.io.File;

public class ShortcutDTO {
    public String getName() {
        return name;
    }

    public File getIcon() {
        return icon;
    }

    private final String name;
    private final File icon;

    public ShortcutDTO(Builder builder) {
        this.name = builder.name;
        this.icon = builder.icon;
    }

    public static class Builder {
        private File icon;
        private String name;

        public ShortcutDTO.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public ShortcutDTO.Builder withIcon(File icon) {
            this.icon = icon;
            return this;
        }

        public ShortcutDTO build() {
            return new ShortcutDTO(this);
        }

    }
}
