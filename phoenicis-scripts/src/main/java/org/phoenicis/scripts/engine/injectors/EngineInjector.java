package org.phoenicis.scripts.engine.injectors;

import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.phoenicis.scripts.exceptions.ScriptException;

/**
 * Injects some code into a Script Engine
 */
public interface EngineInjector {
    /**
     * Injects component into an engine engine
     *
     * @param phoenicisScriptEngine The engine to be injected in
     */
    void injectInto(PhoenicisScriptEngine phoenicisScriptEngine);

    /**
     * Throws a ScriptException error (can be use as a lambda)
     *
     * @param parentException Parent exception
     */
    default void throwException(Exception parentException) {
        throw new ScriptException(parentException);
    }
}
