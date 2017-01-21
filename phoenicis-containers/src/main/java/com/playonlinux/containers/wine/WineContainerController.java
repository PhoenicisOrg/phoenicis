package com.playonlinux.containers.wine;

import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import com.playonlinux.tools.system.OperatingSystemFetcher;
import com.playonlinux.tools.system.terminal.TerminalOpener;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WineContainerController {
    private final ScriptInterpreter scriptInterpreter;
    private final TerminalOpener terminalOpener;
    private final String wineEnginesPath;
    private final OperatingSystemFetcher operatingSystemFetcher;

    public WineContainerController(ScriptInterpreter scriptInterpreter,
                                   TerminalOpener terminalOpener,
                                   String wineEnginesPath,
                                   OperatingSystemFetcher operatingSystemFetcher) {
        this.scriptInterpreter = scriptInterpreter;
        this.terminalOpener = terminalOpener;
        this.wineEnginesPath = wineEnginesPath;
        this.operatingSystemFetcher = operatingSystemFetcher;
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

    public void openTerminalInPrefix(WinePrefixDTO winePrefixDTO) {
        final Map<String, String> environment = new HashMap<>();
        environment.put("WINEPREFIX", winePrefixDTO.getPath());
        environment.put("PATH", fetchWineVersionPath(winePrefixDTO) + "/bin/" + ":$PATH");
        terminalOpener.openTerminal(winePrefixDTO.getPath(), environment);
    }

    private String fetchWineVersionPath(WinePrefixDTO winePrefixDTO) {
        return wineEnginesPath +
                "/" +
                winePrefixDTO.getDistribution() +
                "-" +
                operatingSystemFetcher.fetchCurrentOperationSystem().getWinePackage() +
                "-" +
                winePrefixDTO.getArchitecture() +
                "/" +
                winePrefixDTO.getVersion();
    }
}
