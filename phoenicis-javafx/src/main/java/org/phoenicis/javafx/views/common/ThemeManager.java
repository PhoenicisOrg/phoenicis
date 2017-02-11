package org.phoenicis.javafx.views.common;

public class ThemeManager {
    private Themes currentTheme;

    public ThemeManager() {
        currentTheme = Themes.DEFAULT;
    }

    public ThemeManager(Themes theme) {
        currentTheme = theme;
    }

    public Themes getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Themes theme) {
        currentTheme = theme;
    }
}
