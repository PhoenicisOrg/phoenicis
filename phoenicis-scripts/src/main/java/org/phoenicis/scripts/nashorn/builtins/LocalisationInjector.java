package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;

public class LocalisationInjector implements EngineInjector {
    @Override
    public void inject(NashornEngine nashornEngine) {
        nashornEngine.eval("var tr = Packages.org.phoenicis.configuration.localisation.Localisation.tr;",
                this::throwException);
    }
}
