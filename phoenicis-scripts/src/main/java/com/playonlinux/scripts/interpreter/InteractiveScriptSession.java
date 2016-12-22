package com.playonlinux.scripts.interpreter;

import java.util.function.Consumer;

public interface InteractiveScriptSession {
    void eval(String evaluation, Consumer<String> responseCallback, Consumer<Exception> errorCallback);
}
