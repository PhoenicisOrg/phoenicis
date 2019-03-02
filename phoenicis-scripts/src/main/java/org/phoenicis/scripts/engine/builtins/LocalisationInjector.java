package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.engine.PhoenicisScriptEngine;

/**
 * Injects tr() function into a Script Engine
 */
public class LocalisationInjector implements EngineInjector {
    @Override
    public void injectInto(PhoenicisScriptEngine phoenicisScriptEngine) {
        phoenicisScriptEngine.eval("var tr = Packages.org.phoenicis.configuration.localisation.Localisation.tr;",
                this::throwException);
    }
}
