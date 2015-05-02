package com.playonlinux.injection;

public class InjectionException extends Throwable {
    private final String error;

    public InjectionException(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return this.error;
    }
}
