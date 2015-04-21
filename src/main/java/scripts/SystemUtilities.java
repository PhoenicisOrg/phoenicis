package scripts;

import java.io.File;

public class SystemUtilities {
    final static int KILOBYTE = 1024;
    // TODO: Find a name that do not use the verb "get"

    public static long getFreeSpace(File directory) {
        return directory.getFreeSpace() / KILOBYTE;
    }

    public static long getFreeSpace(String directory) {
        return getFreeSpace(new File(directory));
    }
}
