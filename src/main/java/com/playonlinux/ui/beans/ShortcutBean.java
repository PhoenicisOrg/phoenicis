package com.playonlinux.ui.beans;

import java.io.File;

public class ShortcutBean {
    public String getName() {
        return name;
    }

    public File getIcon() {
        return icon;
    }

    private final String name;
    private final File icon;

    public ShortcutBean(Builder builder) {
        this.name = builder.name;
        this.icon = builder.icon;
    }

    public static class Builder {
        private File icon;
        private String name;

        public ShortcutBean.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public ShortcutBean.Builder withIcon(File icon) {
            this.icon = icon;
            return this;
        }

        public ShortcutBean build() {
            return new ShortcutBean(this);
        }

    }
}
