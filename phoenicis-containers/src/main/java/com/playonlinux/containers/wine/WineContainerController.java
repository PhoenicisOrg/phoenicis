package com.playonlinux.containers.wine;

import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.function.Consumer;

public class WineContainerController {
    private final ScriptInterpreter scriptInterpreter;

    public WineContainerController(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    public void repairPrefix(WinePrefixDTO winePrefix,
                             Runnable doneCallback,
                             Consumer<Exception> errorCallback) {
        // FIXME
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                            shortcutReader.callMember("prefix", winePrefix.getName());
                            shortcutReader.callMember("run", "wineboot");
                            shortcutReader.callMember("wait");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }

    public void killProcesses(WinePrefixDTO winePrefix,
                              Runnable doneCallback,
                              Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                            shortcutReader.callMember("prefix", winePrefix.getName());
                            shortcutReader.callMember("kill");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }

    public void runInPrefix(WinePrefixDTO winePrefix,
                            String command,
                            Runnable doneCallback,
                            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                            shortcutReader.callMember("prefix", winePrefix.getName());
                            shortcutReader.callMember("run", command);
                            shortcutReader.callMember("wait");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }
}
