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
public final class BashEnvironmentHelper {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    private BashEnvironmentHelper() {
        // This is a static class, it should never be instantiated
    }

    public static OperatingSystem getOperatinSystem() throws ScriptFailureException {
        try {
            return OperatingSystem.fetchCurrentOperationSystem();
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }
    }

    public static Architecture getArchitecture() throws ScriptFailureException {
        try {
            return Architecture.fetchCurrentArchitecture();
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }
    }

    public static String getUserRoot() throws ScriptFailureException {
        return playOnLinuxContext.loadProperties().getProperty("application.user.root");
    }

    public static String getEnvironmentVar(String variable) throws ScriptFailureException {
        try {
            Map<String, String> playonOnLinuxEnvironment = playOnLinuxContext.getSystemEnvironment();
            Map<String,String> systemEnvironment = System.getenv();
            
            mergeEnvironmentVariables(systemEnvironment, playonOnLinuxEnvironment, variable);
            return playonOnLinuxEnvironment.get(variable);
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }
    }

    public static String getPath() throws ScriptFailureException {
        return getEnvironmentVar("PATH");
    }

    public static String getLibraryPath() throws ScriptFailureException {
        return getEnvironmentVar("LD_LIBRARY_PATH");
    }

    public static String getDyldLibraryPath() throws ScriptFailureException {
        return getEnvironmentVar("DYLD_LIBRARY_PATH");
    }

}
