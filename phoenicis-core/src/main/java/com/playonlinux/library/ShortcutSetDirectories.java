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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.playonlinux.filesystem.DirectoryWatcherFiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ShortcutSetDirectories implements AutoCloseable {
    private final DirectoryWatcherFiles iconDirectory;
    private final DirectoryWatcherFiles shortcutDirectory;
    private final URL defaultIcon;
    private final List<ShortcutFiles> shortcutFiles;

    private Consumer<List<ShortcutFiles>> onChange;

    public ShortcutSetDirectories(DirectoryWatcherFiles shortcutDirectory, DirectoryWatcherFiles iconDirectory,
            URL defaultIcon) {
        this.shortcutFiles = new ArrayList<>();
        this.iconDirectory = iconDirectory;
        this.defaultIcon = defaultIcon;
        this.shortcutDirectory = shortcutDirectory;

        this.shortcutDirectory.setOnChange(this::update);
    }

    public synchronized List<ShortcutFiles> getShortcutFiles() {
        return shortcutFiles;
    }

    public void update(List<File> files) {
        getShortcutFiles().clear();
        for (File shortcutFile : files) {
            try {
                URL iconURL;
                File iconFile = new File(iconDirectory.getObservedDirectory().toFile(), shortcutFile.getName());
                if (!iconFile.exists()) {
                    iconURL = defaultIcon;
                } else {
                    iconURL = new URL("file://" + iconFile.getAbsolutePath());
                }

                this.getShortcutFiles().add(new ShortcutFiles(shortcutFile.getName(), iconURL, shortcutFile));
            } catch (IOException e) {
                log.warn("Failed tu update shortcut files", e);
            }
        }

        if (onChange != null) {
            onChange.accept(getShortcutFiles());
        }
    }

    @Override
    public void close() {
        shortcutDirectory.close();
        iconDirectory.close();
    }

    public void setOnChange(Consumer<List<ShortcutFiles>> onChange) {
        this.onChange = onChange;
    }
}
