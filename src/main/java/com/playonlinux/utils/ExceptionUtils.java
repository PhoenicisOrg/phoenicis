package com.playonlinux.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    
    private ExceptionUtils() {
        // Never used
    }
    
    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
