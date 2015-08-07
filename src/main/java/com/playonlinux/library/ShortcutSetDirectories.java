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

package com.playonlinux.library;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.ObservableDirectoryFiles;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.scripts.InstallerException;
import com.playonlinux.core.scripts.ScriptFactory;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Scan
class ShortcutSetDirectories extends ObservableDefaultImplementation<List<Shortcut>>
        implements Observer<ObservableDirectoryFiles, File[]>, Closeable {
    
    @Inject
    static ScriptFactory scriptFactory;
    
    private final ObservableDirectoryFiles iconDirectory;
    private final File configFilesDirectory;
    private final ObservableDirectoryFiles shortcutDirectory;
    private final URL defaultIcon;
    private final List<Shortcut> shortcuts;

    private static final Logger LOGGER = Logger.getLogger(ShortcutSetDirectories.class);

    public ShortcutSetDirectories(ObservableDirectoryFiles shortcutDirectory, ObservableDirectoryFiles iconDirectory,
                                  File configFilesDirectory, URL defaultIcon) {
        this.shortcuts = new ArrayList<>();
        this.iconDirectory = iconDirectory;
        this.configFilesDirectory = configFilesDirectory;
        this.defaultIcon = defaultIcon;
        this.shortcutDirectory = shortcutDirectory;

        shortcutDirectory.addObserver(this);
        iconDirectory.addObserver(this);
    }

    public synchronized List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    @Override
    public void update(ObservableDirectoryFiles observableDirectoryFiles, File[] argument) {
        if(observableDirectoryFiles == shortcutDirectory) {
            getShortcuts().clear();
            for (File shortcutFile : argument) {
                try {
                    URL iconURL;
                    File iconFile = new File(iconDirectory.getObservedDirectory(), shortcutFile.getName());
                    if (!iconFile.exists()) {
                        iconURL = defaultIcon;
                    } else {
                        iconURL = new URL("file://"+iconFile.getAbsolutePath());
                    }

                    File configFile = new File(configFilesDirectory, shortcutFile.getName());

                    Shortcut shortcut;
                    if (configFile.exists()) {
                        shortcut = new Shortcut(shortcutFile.getName(), iconURL, scriptFactory
                                .createInstance(shortcutFile),
                                configFile);
                    } else {
                        shortcut = new Shortcut(shortcutFile.getName(), iconURL, scriptFactory
                                .createInstance(shortcutFile));
                    }
                    this.getShortcuts().add(shortcut);
                } catch (IOException | InstallerException e) {
                    LOGGER.warn(e);
                }
            }
        }
        this.notifyObservers(getShortcuts());
    }


    @Override
    public void close() {
        shortcutDirectory.deleteObserver(this);
        iconDirectory.deleteObserver(this);
        this.deleteObservers();
    }
}
