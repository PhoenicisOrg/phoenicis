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
import com.playonlinux.core.observer.ObservableDefaultDirectoryFiles;
import com.playonlinux.library.dto.InstalledApplicationDTO;
import com.playonlinux.library.dto.LibraryWindowDTO;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.AutoStartedService;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.Observer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Scan
@AutoStartedService(name = "InstalledApplicationsService")
public final class LibraryEntitiesProvider
        extends ObservableDefaultImplementation<LibraryWindowDTO>
        implements Observer<ShortcutSetDirectories, List<Shortcut>>,
                   EntitiesProvider<InstalledApplicationDTO, LibraryWindowDTO> {

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    private ShortcutSetDirectories shortcutSetDirectories;

    private final List<InstalledApplicationDTO> installedApplications = new ArrayList<>();
    private final List<InstalledApplicationDTO> installedApplicationsFiltered = new ArrayList<>();

    private Filter<InstalledApplicationDTO> lastFilter;

    @Override
    public void update(ShortcutSetDirectories observable, List<Shortcut> argument) {
        installedApplications.clear();
        installedApplications.addAll(argument.stream().map(shortcut -> new InstalledApplicationDTO.Builder()
                .withName(shortcut.getShortcutName())
                .withIcon(shortcut.getIconPath())
                .build()).collect(Collectors.toList()));

        applyFilter(lastFilter);
    }

    @Override
    public void applyFilter(Filter<InstalledApplicationDTO> filter) {
        lastFilter = filter;

        installedApplicationsFiltered.clear();
        if(filter != null) {
            installedApplicationsFiltered.addAll(installedApplications.stream().filter(filter::apply).collect(Collectors.toList()));
        } else {
            installedApplicationsFiltered.addAll(installedApplications);
        }

        this.notifyObservers(new LibraryWindowDTO(installedApplicationsFiltered));
    }

    @Override
    public void shutdown() {
        shortcutSetDirectories.close();
        deleteObservers();
    }

    @Override
    public void start() throws ServiceInitializationException {
        final File shortcutDirectory = playOnLinuxContext.makeShortcutsScriptsPath();
        final File iconDirectory = playOnLinuxContext.makeShortcutsIconsPath();
        final File configFilesDirectory = playOnLinuxContext.makeShortcutsConfigPath();
        final URL defaultIcon = playOnLinuxContext.makeDefaultIconURL();

        ObservableDefaultDirectoryFiles shortcutDirectoryObservable;
        ObservableDefaultDirectoryFiles iconDirectoryObservable;
        try {
            shortcutDirectoryObservable = new ObservableDefaultDirectoryFiles(shortcutDirectory);
            iconDirectoryObservable = new ObservableDefaultDirectoryFiles(iconDirectory);
        } catch (PlayOnLinuxException e) {
            throw new ServiceInitializationException(e);
        }

        playOnLinuxBackgroundServicesManager.register(shortcutDirectoryObservable);
        playOnLinuxBackgroundServicesManager.register(iconDirectoryObservable);

        shortcutSetDirectories = new ShortcutSetDirectories(shortcutDirectoryObservable, iconDirectoryObservable,
                configFilesDirectory, defaultIcon);


        shortcutSetDirectories.addObserver(this);
    }




}
