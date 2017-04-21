package org.phoenicis.javafx.views.common;

public class ThemeManager {
    private Theme currentTheme;
    private final String themeUrl = "/org/phoenicis/javafx/themes";
    private String defaultCategoryIconsCss = "";
    private String defaultEngineIconsCss = "";

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
     * 
     * @param resource
     *            theme resource
     * @return
     */
    public boolean resourceExists(String resource) {
        return getClass().getResourceAsStream(
                String.format("%s/%s/%s", themeUrl, currentTheme.getShortName(), resource)) != null;
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
            return String.format("%s/%s/%s", themeUrl, currentTheme.getShortName(), resource);
        } else {
            return String.format("%s/%s/%s", themeUrl, Theme.DEFAULT.getShortName(), resource);
        }
    }

    /**
     * sets the path of the CSS file containing the default category icons from the repository
     *
     * @param defaultCategoryIconsCss path of the default category icons from the repository
     */
    public void setDefaultCategoryIconsCss(String defaultCategoryIconsCss) {
        this.defaultCategoryIconsCss = defaultCategoryIconsCss;
    }

    /**
     * returns the path of the CSS file containing the default category icons from the repository
     *
     * @return path of the default category icons from the repository
     */
    public String getDefaultCategoryIconsCss() {
        return defaultCategoryIconsCss;
    }

    /**
     * sets the path of the CSS file containing the default engine icons from the repository
     *
     * @param defaultEngineIconsCss path of the default engine icons from the repository
     */
    public void setDefaultEngineIconsCss(String defaultEngineIconsCss) {
        this.defaultEngineIconsCss = defaultEngineIconsCss;
    }

    /**
     * returns the CSS file containing the default engine icons from the repository
     *
     * @return default engine icons from the repository
     */
    public String getDefaultEngineIconsCss() {
        return defaultEngineIconsCss;
    }
}
