package com.playonlinux.scripts.interpreter;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

public interface ScriptInterpreter {
    void runScript(String scriptContent, Consumer<Exception> errorCallback);

    default void runScript(File scriptFile, Consumer<Exception> errorCallback) {
        try {
            runScript(IOUtils.toString(new FileInputStream(scriptFile), "UTF-8"), errorCallback);
        } catch (Exception e) {
            errorCallback.accept(e);
        }
    }

    InteractiveScriptSession createInteractiveSession();
}
