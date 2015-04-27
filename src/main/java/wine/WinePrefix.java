package wine;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class WinePrefix {
    private static final String PLAYONLINUX_WINEPREFIX_CONFIGFILE = "playonlinux.cfg";
    private final File winePrefixDirectory;

    public WinePrefix(File winePrefixDirectory) {
        this.winePrefixDirectory = winePrefixDirectory;
        if(!this.winePrefixDirectory.exists()) {
            this.winePrefixDirectory.mkdirs();
        }
    }

    public String fetchVersion() {
        return null;
    }

    public String fetchArchitecture() {
        return null;
    }

    public String getAbsolutePath() {
        return this.winePrefixDirectory.getAbsolutePath();
    }

    public File getWinePrefixDirectory() {
        return this.winePrefixDirectory;
    }

    public long getSize() {
        return FileUtils.sizeOfDirectory(this.winePrefixDirectory);
    }

}
