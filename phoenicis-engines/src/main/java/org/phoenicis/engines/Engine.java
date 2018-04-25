package org.phoenicis.engines;

/**
 * interface which must be implemented by all engines in Javascript
 */
public interface Engine {
    /**
     * fetches the available versions
     * 
     * @return content of JSON file
     */
    String getAvailableVersions();
}
