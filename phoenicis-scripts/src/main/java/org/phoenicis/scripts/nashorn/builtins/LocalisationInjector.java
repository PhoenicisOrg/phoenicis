package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.configuration.localisation.Localisation;
import org.phoenicis.scripts.nashorn.NashornEngine;

import java.util.function.Consumer;

public class LocalisationInjector implements EngineInjector {
    @Override
    public void inject(NashornEngine nashornEngine) {
        nashornEngine.put("tr", (Consumer<Object>) Localisation::tr, this::throwException);
    }
}
