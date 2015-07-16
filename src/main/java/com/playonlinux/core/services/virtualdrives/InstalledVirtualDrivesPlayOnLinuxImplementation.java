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

package com.playonlinux.core.services.virtualdrives;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.dto.ui.VirtualDriveDTO;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.core.observer.ObservableDirectoryFiles;
import com.playonlinux.core.observer.Observer;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Scan
public final class InstalledVirtualDrivesPlayOnLinuxImplementation extends AbstractObservableImplementation
        implements InstalledVirtualDrives, Observer<ObservableDirectoryFiles, File[]> {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    private Iterator<VirtualDriveDTO> virtualdrivesDTOInterator;

    final ObservableDirectoryFiles observableWineprefixes;

    public InstalledVirtualDrivesPlayOnLinuxImplementation() throws PlayOnLinuxException {
        File winePrefixes = playOnLinuxContext.makePrefixesPath();
        observableWineprefixes = new ObservableDirectoryFiles(winePrefixes);

        observableWineprefixes.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(observableWineprefixes);
    }

    protected void finalize() throws Throwable {
        try {
            observableWineprefixes.deleteObserver(this);
        } finally {
            super.finalize();
        }
    }

    @Override
    public void update(ObservableDirectoryFiles observable, File[] directoryContent) {
        virtualdrivesDTOInterator = new Iterator<VirtualDriveDTO>() {
            volatile int i = 0;

            @Override
            public boolean hasNext() {
                return (directoryContent.length > i);
            }

            @Override
            public VirtualDriveDTO next() {
                if(i >= directoryContent.length) {
                    throw new NoSuchElementException();
                }
                VirtualDrive virtualDrive =
                        new VirtualDrive(directoryContent[i]);
                i++;
                return new VirtualDriveDTO.Builder()
                        .withName(virtualDrive.getName())
                        .withIcon(playOnLinuxContext.makeDefaultIconURL())
                        .build();
            }
        };

        this.notifyObservers();
    }

    @Override
    public Iterator<VirtualDriveDTO> iterator() {
        return virtualdrivesDTOInterator;
    }

}
