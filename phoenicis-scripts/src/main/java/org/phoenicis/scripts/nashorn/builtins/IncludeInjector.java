package org.phoenicis.scripts.nashorn.builtins;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;
import org.phoenicis.scripts.nashorn.NashornEngine;

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
    public void injectInto(NashornEngine nashornEngine) {
        final Set<String> includedScripts = new HashSet<>();

        nashornEngine.put("include", (Consumer<String>) argument -> {
            final String script = scriptFetcher.getScript(argument);
            if (script == null) {
                throwException(new ScriptException(argument + " is not found"));
            }

            if (includedScripts.add(argument)) {
                nashornEngine.eval("//# sourceURL=" + argument + "\n" + script,
                        this::throwException);
            }
        }, this::throwException);
    }
}
