package com.phoenicis.library;

import com.phoenicis.library.dto.ShortcutDTO;
import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ShortcutRunner {
    private final ScriptInterpreter scriptInterpreter;

    public ShortcutRunner(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    public void run(ShortcutDTO shortcutDTO) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Shortcuts\", \"Reader\"]);",
                ignored -> {
                    interactiveScriptSession.eval(
                            "new ShortcutReader()",
                            output -> {
                                final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                                shortcutReader.callMember("of", shortcutDTO.getScript());
                                shortcutReader.callMember("run");
                            },
                            this::throwError
                    );
                },
                this::throwError
        );

        System.out.println("I will run");
        System.out.println(shortcutDTO.getScript());
    }

    private void throwError(Exception e) {
        throw new IllegalStateException(e);
    }
}
