package org.phoenicis.scripts.engine;

import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.phoenicis.scripts.engine.implementation.PolyglotScriptEngine;

import java.util.Map;

/**
 * The supported script engine types
 */
public enum ScriptEngineType {
    GRAAL("graal.js") {
        @Override
        public PhoenicisScriptEngine createScriptEngine() {
            return new PolyglotScriptEngine("js",
                    Map.of("js.nashorn-compat", "true",
                            "js.foreign-object-prototype", "true",
                            "js.ecmascript-version", "2021"));
        }
    };

    /**
     * The name of the script engine type
     */
    private final String name;

    /**
     * Constructor
     *
     * @param name The name of the script engine type
     */
    ScriptEngineType(String name) {
        this.name = name;
    }

    /**
     * Creates a new instance of the {@link ScriptEngineType}
     *
     * @return The new instance of the {@link ScriptEngineType}
     */
    public abstract PhoenicisScriptEngine createScriptEngine();

    @Override
    public String toString() {
        return this.name;
    }
}
