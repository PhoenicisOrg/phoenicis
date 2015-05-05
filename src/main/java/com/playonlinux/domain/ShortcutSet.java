package com.playonlinux.domain;

import com.playonlinux.utils.ObservableDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ShortcutSet extends Observable implements Observer {
    private final ObservableDirectory iconDirectory;
    private final File configFilesDirectory;
    private final ObservableDirectory shortcutDirectory;
    private final File defaultIcon;
    private List<Shortcut> shortcuts;

    public ShortcutSet(ObservableDirectory shortcutDirectory, ObservableDirectory iconDirectory,
                       File configFilesDirectory, File defaultIcon) {
        this.shortcuts = new ArrayList<>();
        this.iconDirectory = iconDirectory;
        this.configFilesDirectory = configFilesDirectory;
        this.defaultIcon = defaultIcon;
        this.shortcutDirectory = shortcutDirectory;

        shortcutDirectory.addObserver(this);
        iconDirectory.addObserver(this);
    }

    synchronized public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    @Override
    synchronized public void update(Observable o, Object arg) {
        if(o == shortcutDirectory) {
            getShortcuts().clear();
            for (File shortcutFile : (File[]) arg) {
                File iconFile = new File(iconDirectory.getObservedDirectory(), shortcutFile.getName());
                if(!iconFile.exists()) {
                    iconFile = defaultIcon;
                }
                File configFile = new File(configFilesDirectory, shortcutFile.getName());

                Shortcut shortcut;
                if (configFile.exists()) {
                    shortcut = new Shortcut(shortcutFile.getName(), iconFile, new Script(shortcutFile), configFile);
                } else {
                    shortcut = new Shortcut(shortcutFile.getName(), iconFile, new Script(shortcutFile));
                }
                this.getShortcuts().add(shortcut);
            }
        }
        this.setChanged();
        this.notifyObservers(getShortcuts());
    }
}
