package org.phoenicis.scripts.exceptions;

import java.util.Stack;

/**
 * An exception that should be thrown in case a circular include has been detected
 */
public class CircularIncludeException extends ScriptException {
    private static String createMessage(String scriptId, Stack<String> includeStack) {
        String includeStackString = String.join(" -> ", includeStack);

        return String.format("Circular include of \"%s\" in (%s)", scriptId, includeStackString);
    }

    public CircularIncludeException(String scriptId, Stack<String> includeStack) {
        super(createMessage(scriptId, includeStack));
    }
}
