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

import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.utils.ReplacableProperties;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class PlayOnLinuxContext {
    private final Properties properties;

    public PlayOnLinuxContext() throws PlayOnLinuxError, IOException {
        this.properties = loadProperties();
    }

    public ReplacableProperties loadProperties() throws PlayOnLinuxError, IOException {
        ReplacableProperties properties = new ReplacableProperties();

        String filename;
        switch (OperatingSystem.fetchCurrentOperationSystem()) {
            case MACOSX:
                filename = "playonmac.properties";
                break;
            case LINUX:
            default:
                filename = "playonlinux.properties";
        }
        properties.load(PlayOnLinuxContext.class.getClassLoader().getResourceAsStream(filename));
        return properties;
    }

    public File makePrefixPathFromName(String prefixName) {
        String prefixPath = String.format("%s/%s",
                this.properties.getProperty("application.user.wineprefix"),
                prefixName
        );
        return new File(prefixPath);
    }

    public File makePrefixesPath() {
        return new File(this.properties.getProperty("application.user.wineprefix"));
    }

    public File makeWinePathFromVersionAndArchitecture(String version, Architecture architecture) throws PlayOnLinuxError {
        String architectureDirectory = String.format("%s-%s",
                OperatingSystem.fetchCurrentOperationSystem().getNameForWinePackages(),
                architecture.getNameForWinePackages()
        );
        String versionPath = String.format("%s/%s/%s",
                this.properties.getProperty("application.user.wineversions"),
                architectureDirectory,
                version
        );
        return new File(versionPath);
    }

    public HashMap<String,String> getSystemEnvironment() throws PlayOnLinuxError {
        HashMap<String, String> systemEnvironment = new HashMap<>();
        switch(OperatingSystem.fetchCurrentOperationSystem()){
            case MACOSX:
                systemEnvironment.put("PATH", this.properties.getProperty("application.environment.path"));
                systemEnvironment.put("LD_LIBRARY_PATH",
                        this.properties.getProperty("application.environment.ld") + ":" + "/usr/X11/lib");
                systemEnvironment.put("DYLD_LIBRARY_PATH", this.properties.getProperty("application.environment.dyld"));
                break;
            case LINUX:
                break;

        }

        return systemEnvironment;
    }

    public File makeShortcutsScriptsPath() {
        return new File(this.properties.getProperty("application.user.shortcuts.scripts"));
    }

    public File makeShortcutsIconsPath() {
        return new File(this.properties.getProperty("application.user.shortcuts.icons"));
    }

    public File makeShortcutsConfigPath() {
        return new File(this.properties.getProperty("application.user.shortcuts.config"));
    }

    public File makeDefaultIconPath() {
        return new File(this.properties.getProperty("application.root.classes"), "playonlinux.png");
    }


}
