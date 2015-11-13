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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.core.utils.ReplacableProperties;
import com.playonlinux.core.version.Version;
import com.playonlinux.engines.wine.WineDistribution;

/**
 * Contains all the environment settings required by PlayOnLinux
 */
public class PlayOnLinuxContext {
    private final Properties properties;

    public PlayOnLinuxContext() {
        this.properties = loadProperties();
    }

    /**
     * Get the name of the property file
     * @return the name of the suitable property file
     * @throws PlayOnLinuxException If the current operating system is unknown
     */
    public String getPropertyFileName() throws PlayOnLinuxException {
        switch (OperatingSystem.fetchCurrentOperationSystem()) {
            case MACOSX:
                return "playonmac.properties";
            case LINUX:
            default:
                return "playonlinux.properties";
        }
    }

    /**
     * Init logging framework
     */
    public void initLogger() {
        try {
            PropertyConfigurator.configure(PlayOnLinuxContext.class.getClassLoader().getResourceAsStream(getPropertyFileName()));
            Logger.getRootLogger().setLevel(Level.INFO);
        } catch (PlayOnLinuxException e) {
            throw new PlayOnLinuxRuntimeException("Cannot initialize logger", e);
        }
    }

    /**
     * Get the properties for the current OS
     * @return The properties
     */
    public ReplacableProperties loadProperties()  {
        ReplacableProperties propertiesBeingLoaded = new ReplacableProperties();

        try(InputStream propertiesToLoad = PlayOnLinuxContext.class.getClassLoader().getResourceAsStream(this.getPropertyFileName())) {
            propertiesBeingLoaded.load(propertiesToLoad);
        } catch (PlayOnLinuxException | IOException e) {
            throw new PlayOnLinuxRuntimeException("Cannot load properties", e);
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

    public File makeContainersPath() {
        return new File(this.properties.getProperty("application.user.wineprefix"));
    }

    public File makeWinePath(Version version, WineDistribution wineDistribution) {
        String versionPath = String.format("%s/%s/%s",
                this.properties.getProperty("application.user.engines.wine"),
                wineDistribution.asNameWithCurrentOperatingSystem(),
                version
        );
        return new File(versionPath);
    }

    public Map<String,String> getSystemEnvironment() {
        Map<String, String> systemEnvironment = new HashMap<>();
        systemEnvironment.put("PATH", this.properties.getProperty("application.environment.path"));
        systemEnvironment.put("LD_LIBRARY_PATH", this.properties.getProperty("application.environment.ld"));
        systemEnvironment.put("DYLD_LIBRARY_PATH", this.properties.getProperty("application.environment.dyld"));

        return systemEnvironment;
    }

    public File makeShortcutsPath() {
        File shortcutPath = new File(this.properties.getProperty("application.user.shortcuts.scripts"));
        if(!shortcutPath.exists()) {
            shortcutPath.mkdirs();
        }
        return shortcutPath;
    }

    public File makeShortcutsIconsPath() {
        return new File(this.properties.getProperty("application.user.shortcuts.icons"));
    }

    public String getUserHome() {
        return System.getProperty("user.home");
    }

    public File makeEnginesPath(String category) {
        return new File(this.properties.getProperty(String.format("application.user.engines.%s", category)));
    }

    public File makeUserRootPath() {
        return new File(this.properties.getProperty("application.user.root"));
    }

    public URL makeDefaultIconURL() {
        return this.getClass().getResource("/playonlinux.png");
    }

    public URL makeInstallersWebserviceUrl() throws MalformedURLException {
        return new URL(this.properties.getProperty("webservice.apps.url"));
    }

    public URL makeWineVersionWebserviceUrl() throws MalformedURLException {
        return new URL(String.format(this.properties.getProperty("webservice.wine.url"),
                OperatingSystem.fetchCurrentOperationSystem().getWinePackage())
        );
    }

    public File makeLocalGeckoPath() {
        final File geckoPath = new File(this.properties.getProperty("application.user.engines.wine.gecko"));
        geckoPath.mkdir();
        return geckoPath;
    }

    public File makeLocalMonoPath() {
        final File monoPath = new File(this.properties.getProperty("application.user.engines.wine.gecko"));
        monoPath.mkdir();
        return monoPath;
    }

}
