package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;
import org.phoenicis.scripts.engine.PhoenicisScriptContext;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Injects Include() function into a Script Engine
 */
public class IncludeInjector implements ScriptContextInjector {
    private final ScriptFetcher scriptFetcher;

    public IncludeInjector(ScriptFetcher scriptFetcher) {
        this.scriptFetcher = scriptFetcher;
    }

    @Override
    public void injectInto(PhoenicisScriptContext phoenicisScriptContext) {
        final Set<String> includedScripts = new HashSet<>();

        phoenicisScriptContext.put("include", (Consumer<String>) argument -> {
            final String script = scriptFetcher.getScript(argument);
            if (script == null) {
                throwException(new ScriptException(argument + " is not found"));
            }

            if (includedScripts.add(argument)) {
                phoenicisScriptContext.eval("//# sourceURL=" + argument + "\n" + script,
                        this::throwException);
            }
        }, this::throwException);
    }
}
