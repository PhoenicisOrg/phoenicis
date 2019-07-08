package org.phoenicis.scripts.engine.injectors;

import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Injects Include() function into a Script Engine
 */
public class IncludeInjector implements EngineInjector {
    private final ScriptFetcher scriptFetcher;

    public IncludeInjector(ScriptFetcher scriptFetcher) {
        this.scriptFetcher = scriptFetcher;
    }

    @Override
    public void injectInto(PhoenicisScriptEngine phoenicisScriptEngine) {
        // store included scripts (include path -> JS object)
        final Map<String, Object> includedScripts = new HashMap<>();

        phoenicisScriptEngine.put("include", (Function<String, Object>) argument -> {
            final String script = scriptFetcher.getScript(argument);
            if (script == null) {
                throwException(new ScriptException(argument + " is not found"));
            }

            if (!includedScripts.containsKey(argument)) {
                includedScripts.put(argument,
                        phoenicisScriptEngine.evalAndReturn("//# sourceURL=" + argument + "\n" + script,
                                this::throwException));
            }

            return includedScripts.get(argument);
        }, this::throwException);
    }
}
