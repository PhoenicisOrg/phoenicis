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

    /**
     * checks if a theme resource exists
     * @param resource theme resource
     * @return
     */
    public boolean resourceExists(String resource) {
        return getClass().getResourceAsStream(getResourceUrl(resource)) != null;
    }

    /**
     * returns the full resource URL for a given theme resource
     * @param resource theme resource
     * @return full resource URL
     */
    public String getResourceUrl(String resource) {
        return String.format("/org/phoenicis/javafx/themes/%s/%s", currentTheme.getShortName(), resource);
    }
}
