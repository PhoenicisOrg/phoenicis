package org.phoenicis.javafx.themes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A theme located inside the classpath of Phoenicis
 */
public class ClasspathTheme extends Theme {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathTheme.class);

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
    public URI getResourceUrl(String resource) {
        final String resourcePath = this.classpathToTheme.endsWith("/") ? this.classpathToTheme + resource
                : this.classpathToTheme + "/" + resource;

        try {
            return getClass().getResource(resourcePath).toURI();
        } catch (URISyntaxException e) {
            LOGGER.error(String.format("Couldn't find resource '%s'", resourcePath), e);

            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }
}
