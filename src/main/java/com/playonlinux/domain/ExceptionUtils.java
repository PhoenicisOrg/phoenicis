package com.playonlinux.domain;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    
    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
