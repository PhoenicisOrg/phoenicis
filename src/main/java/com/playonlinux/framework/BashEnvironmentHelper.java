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
import com.playonlinux.domain.ScriptClass;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.domain.PlayOnLinuxException;

import java.util.Map;

import static com.playonlinux.wine.WineProcessBuilder.mergeEnvironmentVariables;

@ScriptClass
@Scan
@SuppressWarnings("unused")
public final class BashEnvironmentHelper {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    private BashEnvironmentHelper() {
        // This is a static class, it should never be instantiated
    }

    public static OperatingSystem getOperatinSystem() throws PlayOnLinuxException {
        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public static Architecture getArchitecture() throws PlayOnLinuxException {
        return Architecture.fetchCurrentArchitecture();
    }

    public static String getUserRoot() throws PlayOnLinuxException {
        return playOnLinuxContext.loadProperties().getProperty("application.user.root");
    }

    public static String getEnvironmentVar(String variable) throws PlayOnLinuxException {
        Map<String,String> playonOnLinuxEnvironment = playOnLinuxContext.getSystemEnvironment();
        Map<String,String> systemEnvironment = System.getenv();

        mergeEnvironmentVariables(systemEnvironment, playonOnLinuxEnvironment, variable);
        return playonOnLinuxEnvironment.get(variable);
    }

    public static String getPath() throws PlayOnLinuxException {
        return getEnvironmentVar("PATH");
    }

    public static String getLibraryPath() throws PlayOnLinuxException {
        return getEnvironmentVar("LD_LIBRARY_PATH");
    }

    public static String getDyldLibraryPath() throws PlayOnLinuxException {
        return getEnvironmentVar("DYLD_LIBRARY_PATH");
    }

}
