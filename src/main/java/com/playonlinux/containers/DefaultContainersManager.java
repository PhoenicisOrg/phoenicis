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

package com.playonlinux.containers;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.config.CompatibleConfigFileFormat;
import com.playonlinux.core.config.ConfigFile;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.*;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Scan
public class DefaultContainersManager
        extends ObservableDefaultImplementation<ContainersManager>
        implements ContainersManager {

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager playOnLinuxBackgroundServicesManager;

    private ObservableDirectoryFiles containersDirectoryObservable;
    private final List<AbstractContainer<?>> containers = new ArrayList<>();

    public List<AbstractContainer> getContainers() {
        return new ArrayList<>(containers);
    }

    @Override
    public void update(ObservableDirectoryFiles observable, File[] argument) {
        containers.clear();
        for(File file: argument) {
            final ConfigFile containerConfigFile = new CompatibleConfigFileFormat(new File(file, "playonlinux.cfg"));
            String containerType = containerConfigFile.readValue("containerType");
            if(StringUtils.isBlank(containerType)) {
                containerType = "WinePrefix";
            }

            /* TODO: Improve abstraction here */
            if("WinePrefix".equals(containerType)) {
                containers.add(new WinePrefixContainer(file));
            }
        }

        notifyObservers(this);
    }

    @Override
    public void shutdown() {
        containersDirectoryObservable.deleteObserver(this);
    }

    @Override
    public void init() throws ServiceInitializationException {
        final File containersDirectory = playOnLinuxContext.makeContainersPath();
        try {
            containersDirectoryObservable = new ObservableDirectoryFiles(containersDirectory);
        } catch (PlayOnLinuxException e) {
            throw new ServiceInitializationException(e);
        }

        playOnLinuxBackgroundServicesManager.register(containersDirectoryObservable);

        containersDirectoryObservable.addObserver(this);
    }


}
