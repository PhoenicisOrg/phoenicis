package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;
import org.phoenicis.scripts.engine.PhoenicisScriptEngine;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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

        phoenicisScriptEngine.put("include", (Consumer<String>) argument -> {
            final String script = scriptFetcher.getScript(argument);
            if (script == null) {
                throwException(new ScriptException(argument + " is not found"));
            }

            if (includedScripts.add(argument)) {
                phoenicisScriptEngine.eval("//# sourceURL=" + argument + "\n" + script,
                        this::throwException);
            }
        }, this::throwException);
    }
}
