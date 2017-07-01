package org.phoenicis.scripts.nashorn.builtins;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;
import org.phoenicis.scripts.nashorn.NashornEngine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
        final Set<List<String>> includedScripts = new HashSet<>();

        nashornEngine.put("include", (Consumer<ScriptObjectMirror>) args -> {
            final String[] arguments = args.to(String[].class);
            final String script = scriptFetcher.getScript(arguments);
            if (script == null) {
                throwException(new ScriptException(Arrays.asList(arguments).toString() + " is not found"));
            }

            if (includedScripts.add(Arrays.asList(arguments))) {
                nashornEngine.eval("//# sourceURL=" + Arrays.asList(arguments).toString() + "\n" + script,
                        this::throwException);
            }
        }, this::throwException);
    }
}
