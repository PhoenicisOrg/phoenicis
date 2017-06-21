package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;

/**
 * Injects some code into a Script Engine
 */
public interface EngineInjector {
    void inject(NashornEngine nashornEngine);

    default void throwException(Exception e) {
        throw new org.phoenicis.scripts.interpreter.ScriptException(e);
    }

}
