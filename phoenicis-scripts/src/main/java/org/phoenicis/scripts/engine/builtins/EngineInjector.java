package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.engine.PhoenicisScriptEngine;

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
        throw new org.phoenicis.scripts.interpreter.ScriptException(parentException);
    }
}
