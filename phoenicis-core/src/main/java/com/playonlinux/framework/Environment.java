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

import static com.playonlinux.core.EnvironmentUtils.mergeEnvironmentVariables;

import java.util.Map;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.scripts.ScriptClass;
import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;

@ScriptClass
@Scan
public final class Environment {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private Environment() {
        // This is a static class, it should never be instantiated
    }

    public static OperatingSystem getOperatinSystem() {
        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public static Architecture getArchitecture() {
        return Architecture.fetchCurrentArchitecture();
    }

    public static String getUserRoot() {
        return playOnLinuxContext.loadProperties().getProperty("application.user.root");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getEnvironmentVar(String variable) {
        Map<String, String> playonOnLinuxEnvironment = playOnLinuxContext.getSystemEnvironment();
        Map<String,String> systemEnvironment = System.getenv();

        mergeEnvironmentVariables(systemEnvironment, playonOnLinuxEnvironment, variable);
        return playonOnLinuxEnvironment.get(variable);
    }

    public static String getShortcutsPath() {
        return playOnLinuxContext.makeShortcutsPath().getAbsolutePath();
    }

    public static String getPath() {
        return getEnvironmentVar("PATH");
    }

    public static String getLibraryPath() {
        return getEnvironmentVar("LD_LIBRARY_PATH");
    }

    public static String getDyldLibraryPath() {
        return getEnvironmentVar("DYLD_LIBRARY_PATH");
    }

}
