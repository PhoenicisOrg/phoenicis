package com.playonlinux.javafx.views.mainwindow.console;

public enum ConsoleTextType {
    NORMAL("normal"),
    DEFAULT("default"),
    ERROR("error");

    private final String cssName;

    ConsoleTextType(String cssName) {
        this.cssName = cssName;
    }

    @Override
    public String toString() {
        return this.cssName;
    }
}
