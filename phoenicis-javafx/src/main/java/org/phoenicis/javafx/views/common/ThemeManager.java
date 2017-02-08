package org.phoenicis.javafx.views.common;

public class ThemeManager {
    private static ThemeManager singleton = new ThemeManager();
    private Themes currentTheme;

    public static ThemeManager getInstance( ) {
        return singleton;
    }

    private ThemeManager() {
        currentTheme = Themes.DEFAULT;
    }

    public Themes getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Themes theme) {
        currentTheme = theme;
    }
}
