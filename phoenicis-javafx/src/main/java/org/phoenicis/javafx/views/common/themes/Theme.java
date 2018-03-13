package org.phoenicis.javafx.views.common.themes;

import java.net.URI;

/**
 * A theme for Phoenicis
 *
 * @author marc
 * @since 23.05.17
 */
public abstract class Theme {

    /**
     * The name of this theme
     */
    protected String name;

    /**
     * A shortened name of this theme
     */
    protected String shortName;

    /**
     * Constructor
     * @param name The name of the theme
     * @param shortName A shortened name of the theme
     */
    protected Theme(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public abstract URI getResourceUrl(String resource);
}
