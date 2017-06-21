package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;

/**
 * Injects some code into a Script Engine
 */
public interface EngineInjector {
    /**
     * Injects component into a nashorn engine
     *
     * @param nashornEngine The engine to be injected in
     */
    void injectInto(NashornEngine nashornEngine);

    /**
     * Throws a ScriptException error (can be use as a lambda)
     *
     * @param parentException Parent exception
     */
    default void throwException(Exception parentException) {
        throw new org.phoenicis.scripts.interpreter.ScriptException(parentException);
    }

}
