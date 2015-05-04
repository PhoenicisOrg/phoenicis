package com.playonlinux.domain;

public class PlayOnLinuxError extends Throwable {
    private final String message;

    public PlayOnLinuxError(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
