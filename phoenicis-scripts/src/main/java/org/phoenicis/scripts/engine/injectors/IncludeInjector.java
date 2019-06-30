package org.phoenicis.scripts.engine.injectors;

import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;

import java.util.HashSet;
import java.util.Set;
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
        final Set<String> includedScripts = new HashSet<>();

        phoenicisScriptEngine.put("include", (Function<String, Object>) argument -> {
            final String script = scriptFetcher.getScript(argument);
            if (script == null) {
                throwException(new ScriptException(argument + " is not found"));
            }

            if (includedScripts.add(argument)) {
                return phoenicisScriptEngine.evalAndReturn("//# sourceURL=" + argument + "\n" + script,
                        this::throwException);
            }

            return null;
        }, this::throwException);
    }
}
