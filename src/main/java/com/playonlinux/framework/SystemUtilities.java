package com.playonlinux.framework;

import java.io.File;

@ScriptClass
@SuppressWarnings("unused")
public class SystemUtilities {
    final static int KILOBYTE = 1024;
    // TODO: Find a name that do not use the verb "get"

    public static long getFreeSpace(File directory) {
        return directory.getUsableSpace() / KILOBYTE;
    }

    public static long getFreeSpace(String directory) {
        return getFreeSpace(new File(directory));
    }
}
