package org.phoenicis.javafx.views.common;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

public class ThemeManager {
    private ObjectProperty<Theme> currentTheme;
    private final String themeUrl = "/org/phoenicis/javafx/themes";

    public ThemeManager() {
        currentTheme = new SimpleObjectProperty<Theme>(Theme.DEFAULT);
    }

    public ThemeManager(Theme theme) {
    	currentTheme = new SimpleObjectProperty<Theme>(theme);
    }

    public Theme getCurrentTheme() {
        return currentTheme.get();
    }

    public void setCurrentTheme(Theme theme) {
        currentTheme.set(theme);
    }
    
    public void addListener(ChangeListener<Theme> changeListener) {
    	currentTheme.addListener(changeListener);
    }

    /**
     * checks if a theme resource exists
     * 
     * @param resource
     *            theme resource
     * @return
     */
    public boolean resourceExists(String resource) {
        return getClass().getResourceAsStream(
                String.format("%s/%s/%s", themeUrl, currentTheme.get().getShortName(), resource)) != null;
    }

    /**
     * returns the full resource URL for a given theme resource
     * 
     * @param resource
     *            theme resource
     * @return full resource URL, falls back to default if resource does not
     *         exist in theme
     */
    public String getResourceUrl(String resource) {
        // check if theme contains resource
        if (resourceExists(resource)) {
            return String.format("%s/%s/%s", themeUrl, currentTheme.get().getShortName(), resource);
        } else {
            return String.format("%s/%s/%s", themeUrl, Theme.DEFAULT.getShortName(), resource);
        }
    }
}
