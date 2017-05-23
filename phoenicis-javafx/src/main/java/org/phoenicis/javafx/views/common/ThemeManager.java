package org.phoenicis.javafx.views.common;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.views.common.themes.Theme;
import org.phoenicis.javafx.views.common.themes.Themes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ThemeManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeManager.class);

    /**
     * The currently selected theme
     */
    private Theme currentTheme;

    /**
     * A list of urls to the activated stylesheets
     */
    private Optional<PhoenicisScene> scene;

    private Optional<String> defaultCategoryIconsCss;
    private Optional<String> defaultEngineIconsCss;

    public ThemeManager(Theme currentTheme) {
        this.scene = Optional.empty();
        this.defaultCategoryIconsCss = Optional.empty();
        this.defaultEngineIconsCss = Optional.empty();

        this.setCurrentTheme(currentTheme);
    }

    public void setScene(PhoenicisScene scene) {
        this.scene = Optional.of(scene);

        this.refreshTheme();
    }

    public Theme getCurrentTheme() {
        return this.currentTheme;
    }

    public void setCurrentTheme(Theme theme) {
        LOGGER.info(String.format("Setting theme to '%s'", theme.getName()));

        this.currentTheme = theme;

        this.refreshTheme();
    }

    public void refreshTheme() {
        LOGGER.info("Refreshing currently shown theme");

        this.scene.ifPresent(scene -> {
            ObservableList<String> stylesheets = scene.getStylesheets();

            stylesheets.clear();
            defaultCategoryIconsCss.ifPresent(stylesheets::add);
            defaultEngineIconsCss.ifPresent(stylesheets::add);

            LOGGER.info(String.format("Loading default theme at '%s'", Themes.DEFAULT.getResourceUrl("main.css")));
            stylesheets.add(Themes.DEFAULT.getResourceUrl("main.css"));

            if (!currentTheme.equals(Themes.DEFAULT)) {
                LOGGER.info(String.format("Loading '%s' theme at '%s'", currentTheme.getName(),
                        currentTheme.getResourceUrl("main.css")));
                stylesheets.add(currentTheme.getResourceUrl("main.css"));
            }
        });
    }

    /**
     * sets the path of the CSS file containing the default category icons from
     * the repository
     *
     * @param defaultCategoryIconsCss path of the default category icons from the repository
     */
    public void setDefaultCategoryIconsCss(String defaultCategoryIconsCss) {
        this.defaultCategoryIconsCss = Optional.of(defaultCategoryIconsCss);

        this.refreshTheme();
    }

    /**
     * sets the path of the CSS file containing the default engine icons from
     * the repository
     *
     * @param defaultEngineIconsCss path of the default engine icons from the repository
     */
    public void setDefaultEngineIconsCss(String defaultEngineIconsCss) {
        this.defaultEngineIconsCss = Optional.of(defaultEngineIconsCss);

        this.refreshTheme();
    }
}
