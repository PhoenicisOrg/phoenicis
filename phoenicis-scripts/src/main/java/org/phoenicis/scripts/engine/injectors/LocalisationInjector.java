package org.phoenicis.scripts.engine.injectors;

import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;

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
