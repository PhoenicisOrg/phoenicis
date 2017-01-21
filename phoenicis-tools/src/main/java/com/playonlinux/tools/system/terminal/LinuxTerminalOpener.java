package com.playonlinux.tools.system.terminal;

import java.util.Map;

class LinuxTerminalOpener implements TerminalOpener {
    @Override
    public void openTerminal(String workingDirectory, Map<String, String> environmentVariables) {
        throw new UnsupportedOperationException();
    }
}
