package wine;

import java.io.File;

public class WinePrefix {
    private static final String PLAYONLINUX_WINEPREFIX_CONFIGFILE = "playonlinux.cfg";
    private final File winePrefixDirectory;

    WinePrefix(File winePrefixDirectory) {
        this.winePrefixDirectory = winePrefixDirectory;
    }
}
