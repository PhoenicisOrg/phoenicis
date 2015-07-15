package com.playonlinux.core.scripts;

import com.playonlinux.app.PlayOnLinuxException;

public class InstallerException extends PlayOnLinuxException {
    private static final String DEFAULT_MESSAGE = "The scripts has encountered a fatal error";

    public InstallerException() {
        super(DEFAULT_MESSAGE);
    }
    public InstallerException(String message) {
        super(message);
    }
    public InstallerException(String message, Throwable parent) {
        super(message, parent);
    }
    public InstallerException(Throwable parent) {
        super(DEFAULT_MESSAGE, parent);
    }
}
