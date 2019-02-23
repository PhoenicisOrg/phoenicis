package org.phoenicis.javafx.themes;

import java.net.URI;

/**
 * A theme for Phoenicis
 */
public abstract class Theme {
    /**
     * The name of this theme
     */
    private final String name;

    /**
     * A shortened name of this theme
     */
    private final String shortName;

    /**
     * Constructor
     *
     * @param name The name of the theme
     * @param shortName A shortened name of the theme
     */
    protected Theme(String name, String shortName) {
        super();

        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    /**
     * Gets the {@link URI} leading to the given <code>resource</code> of the theme
     *
     * @param resource The searched resource
     * @return The uri leading to the given resource of the theme
     */
    public abstract URI getResourceUrl(String resource);
}
