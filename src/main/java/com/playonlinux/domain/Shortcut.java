package com.playonlinux.domain;

import java.io.File;

public class Shortcut {
    public String getShortcutName() {
        return shortcutName;
    }

    public File getIconPath() {
        return iconPath;
    }

    private final String shortcutName;
    private final File iconPath;
    private final Script runScript;
    private final File configFile;

    public Shortcut(String shorctName, File iconPath, Script runScript) {
        this(shorctName, iconPath, runScript, null);
    }
    public Shortcut(String shortcutName, File iconPath, Script runScript, File configFile) {
        this.shortcutName = shortcutName;
        this.iconPath = iconPath;
        this.runScript = runScript;
        this.configFile = configFile;
    }
}
