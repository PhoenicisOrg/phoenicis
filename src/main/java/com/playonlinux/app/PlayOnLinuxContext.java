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
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.utils.ReplacableProperties;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PlayOnLinuxContext {
    private final Properties properties;

    public PlayOnLinuxContext() {
        this.properties = loadProperties();
    }

    public String getPropertyFileName() throws PlayOnLinuxException {
        switch (OperatingSystem.fetchCurrentOperationSystem()) {
            case MACOSX:
                return "playonmac.properties";
            case LINUX:
            default:
                return "playonlinux.properties";
        }
    }

    public void initLogger() {
        try {
            PropertyConfigurator.configure("/com/playonlinux/"+getPropertyFileName());
        } catch (PlayOnLinuxException e) {
            throw new PlayOnLinuxRuntimeError("Cannot initialize logger", e);
        }
    }

    public ReplacableProperties loadProperties()  {
        ReplacableProperties propertiesBeingLoaded = new ReplacableProperties();

        try {
            String filename = this.getPropertyFileName();
            propertiesBeingLoaded.load(PlayOnLinuxContext.class.getClassLoader().getResourceAsStream(filename));
        } catch (PlayOnLinuxException | IOException e) {
            throw new PlayOnLinuxRuntimeError("Cannot load properties", e);
        }
        return propertiesBeingLoaded;
    }

    public File makePrefixPathFromName(String prefixName) {
        String prefixPath = String.format("%s/%s",
                this.properties.getProperty("application.user.wineprefix"),
                prefixName
        );
        return new File(prefixPath);
    }

    public String getProperty(String property) {
        return this.properties.getProperty(property);
    }

    public File makePrefixesPath() {
        return new File(this.properties.getProperty("application.user.wineprefix"));
    }

    public File makeWinePathFromVersionAndArchitecture(String version, Architecture architecture) throws PlayOnLinuxException {
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

    public Map<String,String> getSystemEnvironment() throws PlayOnLinuxException {
        Map<String, String> systemEnvironment = new HashMap<>();
        systemEnvironment.put("PATH", this.properties.getProperty("application.environment.path"));
        systemEnvironment.put("LD_LIBRARY_PATH", this.properties.getProperty("application.environment.ld"));
        systemEnvironment.put("DYLD_LIBRARY_PATH", this.properties.getProperty("application.environment.dyld"));


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

    public URL makeDefaultIconURL() {
        return this.getClass().getResource("/playonlinux.png");
    }

    public URL makeWebserviceUrl() throws MalformedURLException {
        return new URL(this.properties.getProperty("webservice.url"));
    }
}
