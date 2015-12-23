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

package com.playonlinux.engines.wine;

import java.io.File;

import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.version.Version;
import com.playonlinux.ui.api.ProgressControl;

/**
 * PlayOnLinux Wine Version Manager
 */
public interface WineVersionManager extends Observable<WineVersionManager>,
                                            Service {
    /**
     * Install a Wine Version
     *
     * @param wineDistribution {@link WineDistribution} to install
     * @param version {@link Version} to install
     * @param progressControl {@link ProgressControl} to show the progress of the process
     * @throws EngineInstallException if any error occurs
     */
    void install(WineDistribution wineDistribution, Version version, ProgressControl progressControl) throws EngineInstallException;

    /**
     * Install a Wine Version
     *
     * @param wineDistribution {@link WineDistribution} to install
     * @param version {@link Version} to install
     * @param localFile The local file containing the version to install
     * @param progressControl {@link ProgressControl} to show the progress of the process
     * @throws EngineInstallException if any error occurs
     */
    void install(WineDistribution wineDistribution, Version version, File localFile, ProgressControl progressControl) throws EngineInstallException;

    /**
     * Uninstall a wine version
     *
     * @param wineDistribution {@link WineDistribution} to uninstall
     * @param version {@link Version} to uninstall
     * @param progressControl {@link ProgressControl} to show the progress of the process
     * @throws EngineInstallException if any error occurs
     */
    void uninstall(WineDistribution wineDistribution, Version version, ProgressControl progressControl) throws EngineInstallException;
}
