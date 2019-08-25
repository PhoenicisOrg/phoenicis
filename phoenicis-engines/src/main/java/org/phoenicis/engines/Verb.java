package org.phoenicis.engines;

import org.phoenicis.scripts.exceptions.ScriptException;

/**
 * Interface which must be implemented by all Verbs in Javascript
 */
public interface Verb {
    /**
     * Installs the Verb in the given container
     *
     * @param container directory name (not the complete path!) of the container where the Verb shall be installed
     * @throws ScriptException thrown if an error occurred during the installation
     */
    void install(String container) throws ScriptException;
}
