package org.phoenicis.scripts.exceptions;

/**
 * An exception that should be thrown if a script execution error occurred during the include of another scirpt
 */
public class IncludeException extends ScriptException {
    private static String createMessage(String scriptId) {
        return String.format("Error while including script: \"%s\"", scriptId);
    }

    public IncludeException(String scriptId, Exception e) {
        super(createMessage(scriptId), e);
    }
}
