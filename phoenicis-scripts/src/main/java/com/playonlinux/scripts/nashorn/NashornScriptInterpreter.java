package com.playonlinux.scripts.nashorn;

import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;

import java.util.function.Consumer;

public class NashornScriptInterpreter implements ScriptInterpreter {
    private final NashornEngineFactory nashornEngineFactory;

    public NashornScriptInterpreter(NashornEngineFactory nashornEngineFactory) {
        this.nashornEngineFactory = nashornEngineFactory;
    }

    @Override
    public void runScript(String scriptContent, Consumer<Exception> errorCallback) {
        nashornEngineFactory.createEngine().eval(scriptContent, errorCallback);
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        return new NashornInteractiveSession(nashornEngineFactory);
    }


}
