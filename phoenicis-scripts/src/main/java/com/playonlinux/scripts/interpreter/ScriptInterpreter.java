package com.playonlinux.scripts.interpreter;

import java.util.function.Consumer;

public interface ScriptInterpreter {
    void runScript(String scriptContent, Consumer<Exception> errorCallback);

    InteractiveScriptSession createInteractiveSession();
}
