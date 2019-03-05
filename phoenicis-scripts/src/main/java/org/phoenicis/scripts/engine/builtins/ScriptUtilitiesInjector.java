package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.engine.PhoenicisScriptContext;

import java.io.InputStreamReader;

/**
 * Includes utils.js
 */
public class ScriptUtilitiesInjector implements ScriptContextInjector {
    @Override
    public void injectInto(PhoenicisScriptContext phoenicisScriptContext) {
        phoenicisScriptContext.eval(new InputStreamReader(getClass().getResourceAsStream("utils.js")),
                this::throwException);
    }
}
