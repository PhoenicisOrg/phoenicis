package com.playonlinux.tools.system.terminal;

import java.util.Map;

public interface TerminalOpener {
    void openTerminal(String workingDirectory, Map<String, String> environmentVariables);
}
