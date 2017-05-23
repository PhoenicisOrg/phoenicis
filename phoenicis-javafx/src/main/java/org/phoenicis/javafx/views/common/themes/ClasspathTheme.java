package org.phoenicis.javafx.views.common.themes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * A theme located inside the classpath of POL 5
 *
 * @author marc
 * @since 23.05.17
 */
public class ClasspathTheme extends Theme {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathTheme.class);

    private String classpathToTheme;

    /**
     * Constructor
     * @param name The name of the theme
     * @param shortName A shortened name of the theme
     * @param classpathToTheme The classpath leading to the theme folder
     */
    public ClasspathTheme(String name, String shortName, String classpathToTheme) {
        super(name, shortName);

        this.classpathToTheme = classpathToTheme;
    }

    public String getResourceUrl(String resource) {
        if (this.classpathToTheme.endsWith("/")) {
            return classpathToTheme + resource;
        } else {
            return classpathToTheme + "/" + resource;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
