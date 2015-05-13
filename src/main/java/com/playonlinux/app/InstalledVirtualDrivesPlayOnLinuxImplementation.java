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
import com.playonlinux.domain.VirtualDrive;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.common.dtos.VirtualDriveDTO;
import com.playonlinux.utils.ObservableDirectory;

import java.io.File;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Component
public class InstalledVirtualDrivesPlayOnLinuxImplementation extends Observable implements InstalledVirtualDrives, Observer {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private Iterator<VirtualDriveDTO> virtualdrivesDTOInterator;

    public InstalledVirtualDrivesPlayOnLinuxImplementation() throws PlayOnLinuxError {
        File winePrefixes = playOnLinuxContext.makePrefixesPath();
        final ObservableDirectory observableWineprefixes = new ObservableDirectory(winePrefixes);

        observableWineprefixes.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(observableWineprefixes);
    }

    @Override
    public void update(Observable o, Object directoryContent) {
        File[] directoryContentCasted = (File[]) directoryContent;
        virtualdrivesDTOInterator = new Iterator<VirtualDriveDTO>() {
            volatile int i = 0;

            @Override
            public boolean hasNext() {
                return (directoryContentCasted.length > i);
            }

            @Override
            public VirtualDriveDTO next() {
                VirtualDrive virtualDrive =
                        new VirtualDrive(directoryContentCasted[i]);
                i++;
                return new VirtualDriveDTO.Builder()
                        .withName(virtualDrive.getName())
                        .withIcon(playOnLinuxContext.makeDefaultIconPath())
                        .build();
            }
        };

        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public Iterator<VirtualDriveDTO> iterator() {
        return virtualdrivesDTOInterator;
    }
}
