package com.playonlinux.scripts.nashorn;

import com.playonlinux.scripts.interpreter.InteractiveScriptSession;

import java.util.function.Consumer;

public class NashornInteractiveSession implements InteractiveScriptSession {
    private final NashornEngine nashornEngine;

    public NashornInteractiveSession(NashornEngineFactory nashornEngineFactory) {
        this.nashornEngine = nashornEngineFactory.createEngine();
    }

    @Override
    public void eval(String evaluation,
                     Consumer<Object> responseCallback,
                     Consumer<Exception> errorCallback) {
        responseCallback.accept(
                nashornEngine.evalAndReturn(evaluation, errorCallback)
        );
    }
}
