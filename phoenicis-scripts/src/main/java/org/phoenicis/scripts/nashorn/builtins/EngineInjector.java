package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;

public interface EngineInjector {
    void inject(NashornEngine nashornEngine);

    default void throwException(Exception e) {
        throw new org.phoenicis.scripts.interpreter.ScriptException(e);
    }

}
