/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

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
