package org.phoenicis.scripts.engine;

import org.graalvm.polyglot.Value;
import org.junit.Test;
import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ScriptEngineTypeTest {
    @Test
    public void shouldSupportConstDeclarationsInEvaluatedScripts() {
        final PhoenicisScriptEngine scriptEngine = ScriptEngineType.GRAAL.createScriptEngine();

        final Value result = (Value) scriptEngine.evalAndReturn("const answer = 42; answer;",
                exception -> fail(exception.getMessage()));

        assertEquals(42, result.asInt());
    }
}
