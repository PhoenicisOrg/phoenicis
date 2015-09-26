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

import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.ObservableDirectoryFiles;
import com.playonlinux.core.observer.Observer;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ShortcutSetDirectories extends ObservableDefaultImplementation<List<ShortcutFiles>>
        implements Observer<ObservableDirectoryFiles, File[]>, Closeable {
    
    private final ObservableDirectoryFiles iconDirectory;
    private final ObservableDirectoryFiles shortcutDirectory;
    private final URL defaultIcon;
    private final List<ShortcutFiles> shortcutFiles;

    private static final Logger LOGGER = Logger.getLogger(ShortcutSetDirectories.class);

    public ShortcutSetDirectories(ObservableDirectoryFiles shortcutDirectory, ObservableDirectoryFiles iconDirectory,
                                  URL defaultIcon) {
        this.shortcutFiles = new ArrayList<>();
        this.iconDirectory = iconDirectory;
        this.defaultIcon = defaultIcon;
        this.shortcutDirectory = shortcutDirectory;

        shortcutDirectory.addObserver(this);
        iconDirectory.addObserver(this);
    }

    public synchronized List<ShortcutFiles> getShortcutFiles() {
        return shortcutFiles;
    }

    @Override
    public void update(ObservableDirectoryFiles observableDirectoryFiles, File[] argument) {
        if(observableDirectoryFiles == shortcutDirectory) {
            getShortcutFiles().clear();
            for (File shortcutFile : argument) {
                try {
                    URL iconURL;
                    File iconFile = new File(iconDirectory.getObservedDirectory(), shortcutFile.getName());
                    if (!iconFile.exists()) {
                        iconURL = defaultIcon;
                    } else {
                        iconURL = new URL("file://"+iconFile.getAbsolutePath());
                    }

                    this.getShortcutFiles().add(new ShortcutFiles(shortcutFile.getName(), iconURL, shortcutFile));
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
            }
        }
        this.notifyObservers(getShortcutFiles());
    }


    @Override
    public void close() {
        shortcutDirectory.deleteObserver(this);
        iconDirectory.deleteObserver(this);
        this.deleteObservers();
    }
}
