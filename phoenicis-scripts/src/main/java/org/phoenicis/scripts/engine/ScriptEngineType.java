package org.phoenicis.scripts.engine;

import javax.script.ScriptEngineManager;
import java.util.Map;

/**
 * type of the script engine
 */
public enum ScriptEngineType {
    NASHORN("nashorn") {
        @Override
        public PhoenicisScriptEngine createScriptEngine() {
            return new JSAScriptEngine("nashorn");
        }
    },

    GRAAL("graal.js") {
        @Override
        public PhoenicisScriptEngine createScriptEngine() {
            return new PolyglotScriptEngine("js", Map.of("js.nashorn-compat", "true"));
        }
    };

    private final String name;

    ScriptEngineType(String name) {
        this.name = name;
    }

    public abstract PhoenicisScriptEngine createScriptEngine();

    @Override
    public String toString() {
        return this.name;
    }
}
