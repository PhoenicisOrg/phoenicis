package com.playonlinux.ui.impl.javafx.common;

public enum Themes {

    DEFAULT("Default theme"),
    DARK("Dark theme");

    private final String name;

    Themes(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
