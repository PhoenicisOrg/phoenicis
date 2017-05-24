package org.phoenicis.javafx.views.common;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
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
    private ObservableList<String> stylesheets;

    /**
     * The stylesheet to be used inside the {@link org.phoenicis.javafx.views.mainwindow.apps.AppPanel}
     */
    private StringProperty webEngineStylesheet;

    private Optional<String> defaultCategoryIconsCss;
    private Optional<String> defaultEngineIconsCss;

    /**
     * Constructor
     *
     * @param currentTheme The current theme
     */
    public ThemeManager(Theme currentTheme) {
        this.stylesheets = FXCollections.observableArrayList();
        this.webEngineStylesheet = new SimpleStringProperty();

        this.defaultCategoryIconsCss = Optional.empty();
        this.defaultEngineIconsCss = Optional.empty();

        this.setCurrentTheme(currentTheme);
    }

    /**
     * Binds this ThemeManager to the given ObservableList
     *
     * @param destination The to be managed observable list with stylesheet locations
     */
    public void bindStylesheets(ObservableList<String> destination) {
        Bindings.bindContent(destination, this.stylesheets);
    }

    /**
     * Binds this ThemeManager to the given StringProperty
     *
     * @param destination The to be managed stylesheet location
     */
    public void bindWebEngineStylesheet(StringProperty destination) {
        destination.bind(this.webEngineStylesheet);
    }

    public Theme getCurrentTheme() {
        return this.currentTheme;
    }

    public void setCurrentTheme(Theme theme) {
        LOGGER.info(String.format("Setting theme to '%s'", theme.getName()));

        this.currentTheme = theme;

        this.refreshTheme();
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

    public void refreshTheme() {
        LOGGER.info("Refreshing currently shown theme");

        stylesheets.clear();
        defaultCategoryIconsCss.ifPresent(stylesheets::add);
        defaultEngineIconsCss.ifPresent(stylesheets::add);

        LOGGER.info(String.format("Loading default theme at '%s'", Themes.DEFAULT.getResourceUrl("main.css")));
        stylesheets.add(Themes.DEFAULT.getResourceUrl("main.css").toString());

        if (!currentTheme.equals(Themes.DEFAULT)) {
            LOGGER.info(String.format("Loading '%s' theme at '%s'", currentTheme.getName(),
                    currentTheme.getResourceUrl("main.css")));
            stylesheets.add(currentTheme.getResourceUrl("main.css").toString());
        }

        LOGGER.info(
                String.format("Loading WebView stylesheet at '%s'", currentTheme.getResourceUrl("description.css")));
        webEngineStylesheet.set(currentTheme.getResourceUrl("description.css").toString());
    }
}
