package org.phoenicis.javafx.themes;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

/**
 * A theme located inside the classpath of Phoenicis
 */
public class ClasspathTheme extends Theme {
    /**
     * The classpath to the theme
     */
    private final String classpathToTheme;

    /**
     * Constructor
     *
     * @param name The name of the theme
     * @param shortName A shortened name of the theme
     * @param classpathToTheme The classpath leading to the theme folder
     */
    public ClasspathTheme(String name, String shortName, String classpathToTheme) {
        super(name, shortName);

        this.classpathToTheme = classpathToTheme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<URI> getResourceUri(String resource) {
        final String resourcePath = this.classpathToTheme.endsWith("/") ? this.classpathToTheme + resource
                : this.classpathToTheme + "/" + resource;

        final URL resourceUrl = getClass().getResource(resourcePath);

        return Optional.ofNullable(resourceUrl).map(url -> {
            try {
                return url.toURI();
            } catch (URISyntaxException e) {
                return null;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", getName())
                .append("shortName", getShortName())
                .append("classpathToTheme", this.classpathToTheme)
                .toString();
    }
}
