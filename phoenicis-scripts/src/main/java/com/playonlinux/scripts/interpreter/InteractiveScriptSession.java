package com.playonlinux.scripts.interpreter;

import java.util.function.Consumer;

public interface InteractiveScriptSession {
    void eval(String evaluation, Consumer<Object> responseCallback, Consumer<Exception> errorCallback);
}
