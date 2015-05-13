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

package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.domain.Shortcut;
import com.playonlinux.domain.ShortcutSet;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.InstalledApplications;
import com.playonlinux.common.dtos.ShortcutDTO;
import com.playonlinux.utils.ObservableDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Component
public class InstalledApplicationsPlayOnLinuxImplementation extends Observable implements InstalledApplications, Observer {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    ShortcutSet shortcutSet;
    private Iterator<ShortcutDTO> shortcutDtoIterator;

    InstalledApplicationsPlayOnLinuxImplementation() throws PlayOnLinuxError {
        File shortcutDirectory = playOnLinuxContext.makeShortcutsScriptsPath();
        File iconDirectory = playOnLinuxContext.makeShortcutsIconsPath();
        File configFilesDirectory = playOnLinuxContext.makeShortcutsConfigPath();
        File defaultIcon = playOnLinuxContext.makeDefaultIconPath();

        ObservableDirectory shortcutDirectoryObservable = new ObservableDirectory(shortcutDirectory);
        ObservableDirectory iconDirectoryObservable = new ObservableDirectory(iconDirectory);

        playOnLinuxBackgroundServicesManager.register(shortcutDirectoryObservable);
        playOnLinuxBackgroundServicesManager.register(iconDirectoryObservable);

        shortcutSet = new ShortcutSet(shortcutDirectoryObservable, iconDirectoryObservable,
                configFilesDirectory, defaultIcon);

        shortcutSet.addObserver(this);
    }


    @Override
    public synchronized void update(Observable o, Object arg) {
        shortcutDtoIterator = new Iterator<ShortcutDTO>() {
            volatile int i = 0;

            @Override
            public boolean hasNext() {
                return ((ArrayList<Shortcut>) arg).size() > i;
            }

            @Override
            public ShortcutDTO next() {
                Shortcut shortcut = ((ArrayList<Shortcut>) arg).get(i);
                i++;
                return new ShortcutDTO.Builder()
                        .withName(shortcut.getShortcutName())
                        .withIcon(shortcut.getIconPath())
                        .build();
            }
        };

        this.setChanged();
        this.notifyObservers();
    }

    @Override
    synchronized public Iterator<ShortcutDTO> iterator() {
        return this.shortcutDtoIterator;
    }

}
