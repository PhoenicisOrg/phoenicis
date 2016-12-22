package com.playonlinux.scripts.interpreter;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class BackgroundScriptInterpreter implements ScriptInterpreter {
    private final ScriptInterpreter delegated;
    private final ExecutorService executorService;

    public BackgroundScriptInterpreter(ScriptInterpreter delegated, ExecutorService executorService) {
        this.delegated = delegated;
        this.executorService = executorService;
    }

    @Override
    public void runScript(String scriptContent, Consumer<Exception> errorCallback) {
        executorService.execute(() -> delegated.runScript(scriptContent, errorCallback));
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        return (evaluation, responseCallback, errorCallback) -> {
            final InteractiveScriptSession interactiveScriptSession = delegated.createInteractiveSession();
            executorService.execute(() -> interactiveScriptSession.eval(evaluation, responseCallback, errorCallback));
        };
    }
}
