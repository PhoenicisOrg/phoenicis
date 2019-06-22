package org.phoenicis.scripts.engine.injectors;

import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;

import java.io.InputStreamReader;

/**
 * Includes utils.js
 */
public class ScriptUtilitiesInjector implements EngineInjector {
    @Override
    public void injectInto(PhoenicisScriptEngine phoenicisScriptEngine) {
        phoenicisScriptEngine.eval(new InputStreamReader(getClass().getResourceAsStream("utils.js")),
                this::throwException);
    }
}
