package org.phoenicis.javafx.themes;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.javafx.utils.StringBindings;

import java.util.Collections;
import java.util.List;

/**
 * A manager class for the currently selected/used {@link Theme}
 */
public class ThemeManager {
    /**
     * The currently selected theme
     */
    private final ObjectProperty<Theme> currentTheme;

    /**
     * The path to the category icons stylesheet
     */
    private final ObservableList<String> defaultCategoryIconsStylesheets;

    /**
     * The path to the engine icons stylesheet
     */
    private final ObservableList<String> defaultEngineIconsStylesheets;

    /**
     * A list of urls to the activated stylesheets
     */
    private final ObservableList<String> stylesheets;

    /**
     * The stylesheet to be used inside the
     * {@link org.phoenicis.javafx.components.application.control.ApplicationInformationPanel}
     */
    private final StringBinding webEngineStylesheet;

    /**
     * Constructor
     *
     * @param currentTheme The current theme
     */
    public ThemeManager(ObjectProperty<Theme> currentTheme) {
        super();

        this.currentTheme = currentTheme;
        this.webEngineStylesheet = Bindings.when(Bindings.isNotNull(currentTheme))
                .then(StringBindings.map(currentTheme,
                        theme -> theme.getResourceUrl("description.css").toString()))
                .otherwise(Themes.STANDARD.getResourceUrl("description.css").toString());

        this.defaultCategoryIconsStylesheets = FXCollections.observableArrayList();
        this.defaultEngineIconsStylesheets = FXCollections.observableArrayList();

        final ObservableList<String> fallbackStylesheets = FXCollections
                .observableList(getStylesheets(Themes.STANDARD));
        final ObservableList<String> currentStylesheets = CollectionBindings
                .mapToList(currentTheme, this::getNonStandardStylesheets);

        this.stylesheets = ConcatenatedList.create(defaultCategoryIconsStylesheets, defaultEngineIconsStylesheets,
                fallbackStylesheets, currentStylesheets);
    }

    /**
     * Constructor
     *
     * @param currentTheme The current theme
     */
    public ThemeManager(Theme currentTheme) {
        this(new SimpleObjectProperty<>(currentTheme));
    }

    public Theme getCurrentTheme() {
        return this.currentTheme.get();
    }

    public void setCurrentTheme(Theme theme) {
        this.currentTheme.set(theme);
    }

    /**
     * Sets the path of the CSS file containing the default category icons from the repository
     *
     * @param defaultCategoryIconsCss The path of the default category icons from the repository
     */
    public void setDefaultCategoryIconsCss(String defaultCategoryIconsCss) {
        this.defaultCategoryIconsStylesheets.setAll(defaultCategoryIconsCss);
    }

    /**
     * Sets the path of the CSS file containing the default engine icons from the repository
     *
     * @param defaultEngineIconsCss The path of the default engine icons from the repository
     */
    public void setDefaultEngineIconsCss(String defaultEngineIconsCss) {
        this.defaultEngineIconsStylesheets.setAll(defaultEngineIconsCss);
    }

    public StringBinding webEngineStylesheetProperty() {
        return webEngineStylesheet;
    }

    public ObservableList<String> getStylesheets() {
        return this.stylesheets;
    }

    /**
     * Gets a list containing all stylesheets for the given theme
     *
     * @param theme The theme
     * @return A list containing all stylesheets for the given theme
     */
    private List<String> getStylesheets(Theme theme) {
        if (theme == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(theme.getResourceUrl("main.css").toString());
    }

    /**
     * Gets a list containing all stylesheets for the given theme if it is not the standard theme
     *
     * @param theme The theme
     * @return A list containing all stylesheets for the given theme if it is not the standard theme
     */
    private List<String> getNonStandardStylesheets(Theme theme) {
        if (Themes.STANDARD == theme) {
            return Collections.emptyList();
        }

        return getStylesheets(theme);
    }
}
