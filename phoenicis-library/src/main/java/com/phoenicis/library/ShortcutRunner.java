package com.phoenicis.library;

import com.phoenicis.library.dto.ShortcutDTO;
import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.List;
import java.util.function.Consumer;

public class ShortcutRunner {
    private final ScriptInterpreter scriptInterpreter;
    private final LibraryManager libraryManager;

    public ShortcutRunner(ScriptInterpreter scriptInterpreter, LibraryManager libraryManager) {
        this.scriptInterpreter = scriptInterpreter;
        this.libraryManager = libraryManager;
    }

    public void run(String shortcutName, List<String> arguments, Consumer<Exception> errorCallback) {
        run(libraryManager.fetchShortcutsFromName(shortcutName), arguments, errorCallback);
    }

    public void run(ShortcutDTO shortcutDTO, List<String> arguments, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Shortcuts\", \"Reader\"]);",
                ignored -> interactiveScriptSession.eval(
                    "new ShortcutReader()",
                    output -> {
                        final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                        shortcutReader.callMember("of", shortcutDTO.getScript());
                        shortcutReader.callMember("run", arguments);
                    },
                    errorCallback),
                errorCallback
        );
    }

    public void stop(ShortcutDTO shortcutDTO, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Shortcuts\", \"Reader\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new ShortcutReader()",
                        output -> {
                            final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                            shortcutReader.callMember("of", shortcutDTO.getScript());
                            shortcutReader.callMember("stop");
                        },
                        errorCallback),
                errorCallback
        );
    }

}
