package org.phoenicis.tools.lnk;

/**
 * Parsed data immutable structure.
 * Contains only data
 */
public class LnkFile {
    private final boolean isDirectory;
    private final boolean isLocal;
    private final String realFilename;
    private final boolean hasArguments;
    private final LnkStringData stringData;

    public LnkFile(boolean isDirectory,
            boolean isLocal,
            String realFilename,
            boolean hasArguments,
            LnkStringData stringData) {
        this.isDirectory = isDirectory;
        this.isLocal = isLocal;
        this.realFilename = realFilename;
        this.hasArguments = hasArguments;
        this.stringData = stringData;
    }

    /**
     * Tests whether the shortcut points to a directory
     * @return true or false
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Tests whether the shortcut points to a local
     * @return true or false
     */
    public boolean isLocal() {
        return isLocal;
    }

    /**
     * Fetches the real name from the sortcut
     * @return the real file name
     */
    public String getRealFilename() {
        return realFilename;
    }

    /**
     * Tests whether the shortcuts has arguments
     * @return true or false
     */
    public boolean isHasArguments() {
        return hasArguments;
    }

    /**
     * Fetches shortcut string data
     * @see LnkStringData
     * @return The string data
     */
    public LnkStringData getStringData() {
        return stringData;
    }
}
