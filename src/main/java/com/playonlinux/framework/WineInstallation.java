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

package com.playonlinux.framework;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.scripts.ScriptClass;
import com.playonlinux.version.Version;
import com.playonlinux.wine.WineDistribution;

import java.io.File;

@Scan
@ScriptClass
@SuppressWarnings("unused")
public class WineInstallation {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private final Version version;
    private final WineDistribution wineDistribution;

    public WineInstallation(Version version, WineDistribution wineDistribution) {
        this.version = version;
        this.wineDistribution = wineDistribution;
    }

    public com.playonlinux.wine.WineInstallation getInstallation() throws ScriptFailureException {
        return new com.playonlinux.wine.WineInstallation.Builder()
                    .withPath(getInstallationPath())
                    .withApplicationEnvironment(playOnLinuxContext.getSystemEnvironment())
                    .build();
    }

    private File getInstallationPath() throws ScriptFailureException {
        return playOnLinuxContext.makeWinePath(
                version,
                wineDistribution
        );
    }

    public boolean isInstalled() throws ScriptFailureException {
        return getInstallation().exists();
    }

    public void install() {

    }
}
