package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.engine.PhoenicisScriptContext;

/**
 * Injects tr() function into a Script Engine
 */
public class LocalisationInjector implements ScriptContextInjector {
    @Override
    public void injectInto(PhoenicisScriptContext phoenicisScriptContext) {
        phoenicisScriptContext.eval("var tr = Packages.org.phoenicis.configuration.localisation.Localisation.tr;",
                this::throwException);
    }
}
