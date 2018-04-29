package org.phoenicis.engines;

import java.util.Map;

/**
 * interface which must be implemented by all engines in Javascript
 */
public interface Engine {

    /**
     * returns the local directory
     * @param subCategory e.g. upstream-linux-x86
     * @param version e.g. 3.0
     * @return local directory
     */
    String getLocalDirectory(String subCategory, String version);

    /**
     * returns true if the engine is installed
     * @param subCategory e.g. upstream-linux-x86
     * @param version e.g. 3.0
     * @return is engine version installed
     */
    boolean isInstalled(String subCategory, String version);

    /**
     * installs the given engine version
     * @param subCategory e.g. upstream-linux-x86
     * @param version e.g. 3.0
     */
    void install(String subCategory, String version);

    /**
     * deletes the given engine version
     * @param subCategory e.g. upstream-linux-x86
     * @param version e.g. 3.0
     */
    void delete(String subCategory, String version);

    /**
     * fetches the available versions     *
     * @return content of JSON file
     */
    String getAvailableVersions();
}
