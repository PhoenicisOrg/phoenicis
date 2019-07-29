package org.phoenicis.scripts.engine.injectors;

import org.graalvm.polyglot.Value;
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
            if (!includedScripts.containsKey(argument)) {
                final String script = scriptFetcher.getScript(argument);
                if (script == null) {
                    throwException(new ScriptException("Script '" + argument + "' is not found"));
                }

                // wrap the loaded script in a function to prevent it from influencing the main script
                String extendedString = String.format("(module) => { %s }", script);
                Value includeFunction = (Value) phoenicisScriptEngine.evalAndReturn(extendedString,
                        this::throwException);

                // create an empty JS object
                Value module = (Value) phoenicisScriptEngine.evalAndReturn("({})", this::throwException);

                // execute the included function -> populates "module"
                includeFunction.execute(module);

                if (module.hasMember("default")) {
                    includedScripts.put(argument, module.getMember("default"));
                } else {
                    includedScripts.put(argument, module);
                }
            }

            return includedScripts.get(argument);
        }, this::throwException);
    }
}
