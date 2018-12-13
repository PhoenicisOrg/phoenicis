package org.phoenicis.engines;

/**
 * interface which must be implemented by all Verbs in Javascript
 */
public interface Verb {
    /**
     * installs the Verb in the given container
     * @param container directory name (not the complete path!) of the container where the Verb shall be installed
     * @param version version of the Verb
     */
    void install(String container, String version);
}
