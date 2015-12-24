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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.filesystem.DirectoryWatcherFiles;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;

@Scan
public final class LibraryEntitiesProvider
        implements EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> {

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ExecutorService executorService;

    private ShortcutSetDirectories shortcutSetDirectories;

    private final Collection<InstalledApplicationEntity> installedApplications = new ArrayList<>();
    private final List<InstalledApplicationEntity> installedApplicationsFiltered = new ArrayList<>();

    private Predicate<InstalledApplicationEntity> lastFilter;
    private Consumer<LibraryWindowEntity> onChange;

    public void update(List<ShortcutFiles> argument) {
        installedApplications.clear();
        installedApplications
                .addAll(argument
                        .stream().map(shortcut -> new InstalledApplicationEntity.Builder()
                                .withName(shortcut.getShortcutName()).withIcon(shortcut.getIconPath()).build())
                .collect(Collectors.toList()));

        applyFilter(lastFilter);
    }

    @Override
    public void applyFilter(Predicate<InstalledApplicationEntity> filter) {
        lastFilter = filter;

        installedApplicationsFiltered.clear();
        if (filter != null) {
            installedApplicationsFiltered
                    .addAll(installedApplications.stream().filter(filter).collect(Collectors.toList()));
        } else {
            installedApplicationsFiltered.addAll(installedApplications);
        }

        if (onChange != null) {
            onChange.accept(new LibraryWindowEntity(installedApplicationsFiltered));
        }
    }

    @Override
    public void shutdown() {
        shortcutSetDirectories.close();
    }

    @Override
    public void init() throws ServiceInitializationException {
        final File shortcutDirectory = playOnLinuxContext.makeShortcutsPath();
        final File iconDirectory = playOnLinuxContext.makeShortcutsIconsPath();
        final URL defaultIcon = playOnLinuxContext.makeDefaultIconURL();

        DirectoryWatcherFiles shortcutDirectoryObservable;
        DirectoryWatcherFiles iconDirectoryObservable;

        shortcutDirectoryObservable = new DirectoryWatcherFiles(executorService, shortcutDirectory);
        iconDirectoryObservable = new DirectoryWatcherFiles(executorService, iconDirectory);

        shortcutSetDirectories = new ShortcutSetDirectories(shortcutDirectoryObservable, iconDirectoryObservable,
                defaultIcon);

        shortcutSetDirectories.setOnChange(this::update);
    }

    public void setOnChange(Consumer<LibraryWindowEntity> onChange) {
        this.onChange = onChange;
    }

}
