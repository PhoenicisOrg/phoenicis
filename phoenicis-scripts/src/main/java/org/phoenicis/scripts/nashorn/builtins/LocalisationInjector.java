package org.phoenicis.scripts.nashorn.builtins;
import org.phoenicis.scripts.nashorn.NashornEngine;

/**
 * Injects tr() function into a Script Engine
 */
public class LocalisationInjector implements EngineInjector {
    @Override
    public void inject(NashornEngine nashornEngine) {
        nashornEngine.eval("var tr = Packages.org.phoenicis.configuration.localisation.Localisation.tr;",
                this::throwException);
    }
}
