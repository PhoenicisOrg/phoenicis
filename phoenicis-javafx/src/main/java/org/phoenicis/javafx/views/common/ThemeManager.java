package org.phoenicis.javafx.views.common;

public class ThemeManager {
    private Theme currentTheme;

    public ThemeManager() {
        currentTheme = Theme.DEFAULT;
    }

    public ThemeManager(Theme theme) {
        currentTheme = theme;
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Theme theme) {
        currentTheme = theme;
    }
}
