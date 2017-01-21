package com.playonlinux.tools.system.terminal;

public class TerminalOpenerFactory {
    TerminalOpener createInstance(Class<? extends TerminalOpener> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
