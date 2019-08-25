package org.phoenicis.scripts.engine.injectors;

import org.graalvm.polyglot.Value;
import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.phoenicis.scripts.exceptions.CircularIncludeException;
import org.phoenicis.scripts.exceptions.IncludeException;
import org.phoenicis.scripts.exceptions.ScriptException;
import org.phoenicis.scripts.exceptions.ScriptNotFoundException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
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
        // store scripts that are currently in progress
        final Stack<String> includeStack = new Stack<>();
        // store included scripts (include path -> JS object)
        final Map<String, Object> includedScripts = new HashMap<>();

        phoenicisScriptEngine.put("include", (Function<String, Object>) argument -> {
            // prevent circular includes
            if (includeStack.contains(argument)) {
                throw new CircularIncludeException(argument, includeStack);
            }

            includeStack.push(argument);

            if (!includedScripts.containsKey(argument)) {
                final String script = scriptFetcher.getScript(argument);
                if (script == null) {
                    throw new ScriptNotFoundException(argument);
                }

                try {
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
                } catch (ScriptException se) {
                    throw new IncludeException(argument, se);
                }
            }

            includeStack.pop();

            return includedScripts.get(argument);
        }, this::throwException);
    }
}
