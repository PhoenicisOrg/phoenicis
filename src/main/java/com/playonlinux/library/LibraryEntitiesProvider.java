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

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.ObservableDirectoryFiles;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Scan
public final class LibraryEntitiesProvider
        extends ObservableDefaultImplementation<LibraryWindowEntity>
        implements Observer<ShortcutSetDirectories, List<ShortcutFiles>>,
                   EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> {

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    private ShortcutSetDirectories shortcutSetDirectories;

    private final List<InstalledApplicationEntity> installedApplications = new ArrayList<>();
    private final List<InstalledApplicationEntity> installedApplicationsFiltered = new ArrayList<>();

    private Filter<InstalledApplicationEntity> lastFilter;

    @Override
    public void update(ShortcutSetDirectories observable, List<ShortcutFiles> argument) {
        installedApplications.clear();
        installedApplications.addAll(argument.stream().map(shortcut -> new InstalledApplicationEntity.Builder()
                .withName(shortcut.getShortcutName())
                .withIcon(shortcut.getIconPath())
                .build()).collect(Collectors.toList()));

        applyFilter(lastFilter);
    }

    @Override
    public void applyFilter(Filter<InstalledApplicationEntity> filter) {
        lastFilter = filter;

        installedApplicationsFiltered.clear();
        if(filter != null) {
            installedApplicationsFiltered.addAll(installedApplications.stream().filter(filter::apply).collect(Collectors.toList()));
        } else {
            installedApplicationsFiltered.addAll(installedApplications);
        }

        this.notifyObservers(new LibraryWindowEntity(installedApplicationsFiltered));
    }

    @Override
    public void shutdown() {
        shortcutSetDirectories.close();
        deleteObservers();
    }

    @Override
    public void init() throws ServiceInitializationException {
        final File shortcutDirectory = playOnLinuxContext.makeShortcutsPath();
        final File iconDirectory = playOnLinuxContext.makeShortcutsIconsPath();
        final URL defaultIcon = playOnLinuxContext.makeDefaultIconURL();

        ObservableDirectoryFiles shortcutDirectoryObservable;
        ObservableDirectoryFiles iconDirectoryObservable;
        try {
            shortcutDirectoryObservable = new ObservableDirectoryFiles(shortcutDirectory);
            iconDirectoryObservable = new ObservableDirectoryFiles(iconDirectory);
        } catch (PlayOnLinuxException e) {
            throw new ServiceInitializationException(e);
        }

        playOnLinuxBackgroundServicesManager.register(shortcutDirectoryObservable);
        playOnLinuxBackgroundServicesManager.register(iconDirectoryObservable);

        shortcutSetDirectories = new ShortcutSetDirectories(shortcutDirectoryObservable, iconDirectoryObservable, defaultIcon);

        shortcutSetDirectories.addObserver(this);
    }




}
