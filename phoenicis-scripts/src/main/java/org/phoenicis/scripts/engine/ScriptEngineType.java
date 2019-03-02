package org.phoenicis.scripts.engine;

/**
 * type of the script engine
 */
public enum ScriptEngineType {
    NASHORN("nashorn"), GRAAL("graal.js");

    private final String name;

    ScriptEngineType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
