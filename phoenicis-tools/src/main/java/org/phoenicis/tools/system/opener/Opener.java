package org.phoenicis.tools.system.opener;

import java.net.URI;

/**
 * System Opener
 */
@FunctionalInterface
public interface Opener {
    /**
     * Open a file
     * @param file to be opened
     */
    void open(String file);

    /**
     * Open a URL
     * @param url to be opened
     */
    default void open(URI url) {
        open(url.toString());
    }
}
