package com.playonlinux.javafx.views.mainwindow.console;

public enum ConsoleTextType {
    NORMAL("normal"),
    DEFAULT("default"),
    ERROR("error");

    private final String cssName;

    ConsoleTextType(String cssName) {
        this.cssName = cssName;
    }

    public String getCssName() {
        return cssName;
    }

    @Override
    public String toString() {
        return this.cssName;
    }
}
