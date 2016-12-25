package com.playonlinux.cli.setupwindow;

import com.playonlinux.scripts.ui.UIMessageSender;

import java.util.function.Supplier;

class CLIMessageSender implements UIMessageSender {
    @Override
    public <R> R run(Supplier<R> function) {
        return function.get();
    }
}
