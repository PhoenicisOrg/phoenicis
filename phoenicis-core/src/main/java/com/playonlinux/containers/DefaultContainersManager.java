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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.containers.entities.ContainerEntity;
import com.playonlinux.core.config.CompatibleConfigFileFormat;
import com.playonlinux.core.config.ConfigFile;
import com.playonlinux.filesystem.DirectoryWatcherFiles;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;

import lombok.Setter;

@Scan
public class DefaultContainersManager implements ContainersManager {

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ExecutorService executorService;

    @Inject
    static AnyContainerFactory anyContainerFactory;

    private DirectoryWatcherFiles containersDirectoryObservable;
    private final List<Container<? extends ContainerEntity>> containers = new ArrayList<>();
    @Setter
    private Consumer<ContainersManager> onChange;

    @Override
    public List<Container<? extends ContainerEntity>> getContainers() {
        return new ArrayList<>(containers);
    }

    public void update(List<File> files) {
        containers.clear();

        for (File file : files) {
            final ConfigFile containerConfigFile = new CompatibleConfigFileFormat(new File(file, "playonlinux.cfg"));
            String containerType = containerConfigFile.readValue("containerType");
            if (StringUtils.isBlank(containerType)) {
                containerType = "WinePrefix";
            }

            containers.add(anyContainerFactory.createInstance(containerType, file));
        }

        if (onChange != null) {
            onChange.accept(this);
        }
    }

    @Override
    public void shutdown() {
        // Nothing to shutdown
    }

    @Override
    public void init() {
        final File containersDirectory = playOnLinuxContext.makeContainersPath();
        containersDirectoryObservable = new DirectoryWatcherFiles(executorService, containersDirectory.toPath());
        containersDirectoryObservable.setOnChange(this::update);
    }
}
