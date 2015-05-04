package com.playonlinux.domain;

import java.io.File;

public class Shortcut {
    private final String shorctName;
    private final File iconPath;
    private final Script runScript;
    private final File configFile;

    public Shortcut(String shorctName, File iconPath, Script runScript) {
        this(shorctName, iconPath, runScript, null);
    }
    public Shortcut(String shortcutName, File iconPath, Script runScript, File configFile) {
        this.shorctName = shortcutName;
        this.iconPath = iconPath;
        this.runScript = runScript;
        this.configFile = configFile;
    }
}
