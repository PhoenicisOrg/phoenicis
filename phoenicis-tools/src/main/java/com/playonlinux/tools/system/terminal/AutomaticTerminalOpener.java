package com.playonlinux.tools.system.terminal;

import com.playonlinux.tools.system.OperatingSystemFetcher;

import java.util.Map;

public class AutomaticTerminalOpener implements TerminalOpener {
    private final TerminalOpener terminalOpener;

    public AutomaticTerminalOpener(TerminalOpenerFactory terminalOpenerFactory,
                                   OperatingSystemFetcher operatingSystemFetcher) {
        switch (operatingSystemFetcher.fetchCurrentOperationSystem()) {
            case LINUX:
                terminalOpener = terminalOpenerFactory.createInstance(LinuxTerminalOpener.class);
                break;
            case MACOSX:
                terminalOpener = terminalOpenerFactory.createInstance(MacOSTerminalOpener.class);
                break;
            case FREEBSD:
            default:
                terminalOpener = (workingDirectory, environment) -> { throw new UnsupportedOperationException(); };
        }
    }

    @Override
    public void openTerminal(String workingDirectory, Map<String, String> environmentVariables) {
        terminalOpener.openTerminal(workingDirectory, environmentVariables);
    }
}
