package com.playonlinux.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtils {
    
    private ExceptionUtils() {
        // This is a utility class, it should never be instantiated
    }
    
    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
