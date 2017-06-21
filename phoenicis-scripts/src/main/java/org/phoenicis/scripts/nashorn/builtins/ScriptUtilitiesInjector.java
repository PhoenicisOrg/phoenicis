package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;

import java.io.InputStreamReader;

public class ScriptUtilitiesInjector implements EngineInjector {
    @Override
    public void inject(NashornEngine nashornEngine) {
        nashornEngine.eval(new InputStreamReader(getClass().getResourceAsStream("utils.js")), this::throwException);
    }
}
