package org.phoenicis.scripts.engine;

import javax.script.ScriptEngineManager;

/**
 * type of the script engine
 */
public enum ScriptEngineType {
    NASHORN("nashorn") {
        @Override
        public PhoenicisScriptEngine createScriptEngine() {
            return new JSAScriptEngine(new ScriptEngineManager().getEngineByName("nashorn"));
        }
    },

    GRAAL("graal.js") {
        @Override
        public PhoenicisScriptEngine createScriptEngine() {
            return new PolyglotScriptEngine();
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
