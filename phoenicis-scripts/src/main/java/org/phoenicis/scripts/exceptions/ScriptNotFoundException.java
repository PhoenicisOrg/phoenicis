package org.phoenicis.scripts.exceptions;

/**
 * An exception that should be thrown if a script could not be located
 */
public class ScriptNotFoundException extends ScriptException {
    private static String createMessage(String scriptId) {
        return String.format("Script \"%s\" is not found", scriptId);
    }

    public ScriptNotFoundException(String scriptId) {
        super(createMessage(scriptId));
    }
}
