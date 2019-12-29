package org.phoenicis.javafx.themes;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.components.application.control.ApplicationInformationPanel;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.javafx.utils.ObjectBindings;
import org.phoenicis.javafx.utils.StringBindings;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * The path to the library category icons stylesheet
     */
    private final ObservableList<String> defaultLibraryCategoryIconsStylesheets;

    /**
     * The path to the engine icons stylesheet
     */
    private final ObservableList<String> defaultEngineIconsStylesheets;

    /**
     * A list of urls to the activated stylesheets
     */
    private final ObservableList<String> stylesheets;

    /**
     * The stylesheet to be used inside the {@link ApplicationInformationPanel}
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

        this.defaultCategoryIconsStylesheets = FXCollections.observableArrayList();
        this.defaultLibraryCategoryIconsStylesheets = FXCollections.observableArrayList();
        this.defaultEngineIconsStylesheets = FXCollections.observableArrayList();

        // the base stylesheets
        final ObservableList<String> baseStylesheets = FXCollections
                .observableList(getStylesheets(Themes.STANDARD));
        // the currently selected stylesheets (empty if the the selected theme is the standard theme)
        final ObservableList<String> currentStylesheets = CollectionBindings
                .mapToList(currentTheme, this::getNonStandardStylesheets);

        this.stylesheets = ConcatenatedList.create(
                defaultCategoryIconsStylesheets,
                defaultLibraryCategoryIconsStylesheets,
                defaultEngineIconsStylesheets,
                baseStylesheets,
                currentStylesheets);

        final ObjectBinding<Optional<URI>> currentWebEngineUri = ObjectBindings.map(currentTheme,
                theme -> theme.getResourceUri("description.css"));
        final StringBinding currentWebEngineStylesheet = StringBindings.map(currentWebEngineUri,
                uri -> uri.map(URI::toString).orElse(null));

        this.webEngineStylesheet = Bindings
                .when(Bindings.isNull(currentWebEngineStylesheet))
                .then(Themes.STANDARD.getResourceUri("description.css").map(URI::toString)
                        .orElseThrow(
                                () -> new IllegalStateException("Standard theme contains no \"description.css\" file")))
                .otherwise(currentWebEngineStylesheet);
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
     * Sets the path of the CSS file containing the default library category icons
     *
     * @param defaultLibraryCategoryIconsCss The path of the default library category icons
     */
    public void setDefaultLibraryCategoryIconsCss(String defaultLibraryCategoryIconsCss) {
        this.defaultLibraryCategoryIconsStylesheets.setAll(defaultLibraryCategoryIconsCss);
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
        return this.webEngineStylesheet;
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

        // an ordered list of all file names making up a theme
        final List<String> themeResourceFiles = List.of("main.css", "buttons.css");

        // query the theme for the concrete paths leading to the theme resource files
        return themeResourceFiles.stream()
                .map(theme::getResourceUri)
                .flatMap(Optional::stream)
                .map(URI::toString)
                .collect(Collectors.toList());
    }

    /**
     * Gets a list containing all stylesheets for the given theme if it is not the standard theme
     *
     * @param theme The theme
     * @return A list containing all stylesheets for the given theme if it is not the standard theme
     */
    private List<String> getNonStandardStylesheets(Theme theme) {
        return Themes.STANDARD == theme ? Collections.emptyList() : getStylesheets(theme);
    }
}
