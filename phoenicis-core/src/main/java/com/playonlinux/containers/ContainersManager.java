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
import java.util.List;

import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.ObservableDirectoryFiles;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.services.manager.Service;

/**
 * Manage PlayOnLinux containers
 */
public interface ContainersManager extends Observable<ContainersManager>,
                                           Observer<ObservableDirectoryFiles, File[]>,
                                           Service {
    /**
     * Get the list of containers
     * @return The list of containers
     */
    List<AbstractContainer> getContainers();
}
