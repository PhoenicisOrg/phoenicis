package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.engine.PhoenicisScriptContext;

/**
 * Injects some code into a script context
 */
public interface ScriptContextInjector {
    /**
     * Injects component into a script context
     *
     * @param phoenicisScriptContext The context to be injected in
     */
    void injectInto(PhoenicisScriptContext phoenicisScriptContext);

    /**
     * Throws a ScriptException error (can be use as a lambda)
     *
     * @param parentException Parent exception
     */
    default void throwException(Exception parentException) {
        throw new org.phoenicis.scripts.interpreter.ScriptException(parentException);
    }
}
