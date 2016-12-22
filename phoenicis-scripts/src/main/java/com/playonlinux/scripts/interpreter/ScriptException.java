package com.playonlinux.scripts.interpreter;


public class ScriptException extends RuntimeException {
    public ScriptException(Exception e) {
        super(e);
    }

    public ScriptException(String s, Exception e) {
        super(s, e);
    }

    public ScriptException(String s) {
        super(s);
    }
}
