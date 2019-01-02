package org.phoenicis.tools.lnk;

/**
 * Parsed data structure
 */
public class LnkFile {
    private final boolean isDirectory;
    private final boolean isLocal;
    private final String realFilename;
    private final boolean hasArguments;

    public LnkFile(boolean isDirectory,
            boolean isLocal,
            String realFilename,
            boolean hasArguments) {
        this.isDirectory = isDirectory;
        this.isLocal = isLocal;
        this.realFilename = realFilename;
        this.hasArguments = hasArguments;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public String getRealFilename() {
        return realFilename;
    }

    public boolean isHasArguments() {
        return hasArguments;
    }
}
