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
    List<Shortcut> shortcuts;

    public ShortcutSet(ObservableDirectory shortcutDirectory, ObservableDirectory iconDirectory,
                       File configFilesDirectory) {
        this.shortcuts = new ArrayList<>();
        this.iconDirectory = iconDirectory;
        this.configFilesDirectory = configFilesDirectory;

        shortcutDirectory.addObserver(this);
        iconDirectory.addObserver(this);
    }



    @Override
    public void update(Observable o, Object arg) {
        shortcuts.clear();
        for(File shortcutFile: (File[]) arg) {
            File iconFile = new File(iconDirectory.getObservedDirectory(), shortcutFile.getName());
            File configFile = new File(configFilesDirectory, shortcutFile.getName());

            Shortcut shortcut;
            if(configFile.exists()) {
                shortcut = new Shortcut(shortcutFile.getName(), iconFile, new Script(shortcutFile), configFile);
            } else {
                shortcut = new Shortcut(shortcutFile.getName(), iconFile, new Script(shortcutFile));
            }
            this.shortcuts.add(shortcut);
        }
        this.notifyObservers(this.shortcuts);
    }
}
