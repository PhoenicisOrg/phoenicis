package org.phoenicis.javafx.themes;

import java.util.Arrays;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A collection of all builtin themes
 */
public class Themes {
    public static final Theme STANDARD = new ClasspathTheme(tr("Standard theme"), "standard",
            "/org/phoenicis/javafx/themes/standard/");
    public static final Theme DARK = new ClasspathTheme(tr("Dark theme"), "dark", "/org/phoenicis/javafx/themes/dark/");
    public static final Theme BREEZE_DARK = new ClasspathTheme(tr("Breeze Dark theme"), "breezeDark",
            "/org/phoenicis/javafx/themes/breezeDark/");
    public static final Theme UNITY = new ClasspathTheme(tr("Unity theme"), "unity",
            "/org/phoenicis/javafx/themes/unity/");
    public static final Theme MINT_X = new ClasspathTheme(tr("Mint-X theme"), "mint-x",
            "/org/phoenicis/javafx/themes/mint-x/");

    /**
     * Gets an array containing all builtin themes
     *
     * @return An array containing all builtin themes
     */
    public static Theme[] all() {
        return new Theme[] { STANDARD, DARK, BREEZE_DARK, UNITY, MINT_X };
    }

    /**
     * Searches for the builtin theme with the given <code>shortName</code>.
     * If no such builtin theme exists {@link Optional#empty()} is returned
     *
     * @param shortName The short name of the searched theme
     * @return The builtin theme with the given short name or {@link Optional#empty()} if no such theme exists
     */
    public static Optional<Theme> fromShortName(String shortName) {
        return Arrays.stream(all()).filter(theme -> theme.getShortName().equals(shortName)).findAny();
    }
}
