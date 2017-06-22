package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;

import java.io.InputStreamReader;

/**
 * Includes utils.js
 */
public class ScriptUtilitiesInjector implements EngineInjector {
    @Override
    public void injectInto(NashornEngine nashornEngine) {
        nashornEngine.eval(new InputStreamReader(getClass().getResourceAsStream("utils.js")), this::throwException);
    }
}
