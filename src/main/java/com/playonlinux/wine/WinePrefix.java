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

package com.playonlinux.wine;

import com.playonlinux.core.config.CompatibleConfigFileFormat;
import com.playonlinux.core.config.ConfigFile;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.log.LoggerFactory;
import com.playonlinux.core.log.WinePrefixLogger;
import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.Files;
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.core.version.Version;
import com.playonlinux.engines.wine.WineDistribution;
import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryParser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a wineprefix
 */
@Scan
public class WinePrefix implements AutoCloseable {
    @Inject
    static LoggerFactory loggerFactory;

    private static final String PLAYONLINUX_WINEPREFIX_CONFIGFILE = "playonlinux.cfg";
    private static final String SYSTEM_REGISTRY_FILENAME = "system.reg";
    private static final String SYSTEM_REGISTRY_NODENAME = "HKEY_LOCAL_MACHINE";
    private static final String USER_REGISTRY_FILENAME = "user.reg";
    private static final String USER_REGISTRY_NODENAME = "HKEY_CURRENT_USER";
    private static final String EXECUTABLE_EXTENSION = "exe";
    private static final String DRIVE_C = "drive_c";

    private static final String[] SEARCH_EXCLUDED_EXECUTABLE = new String[] {"iexplore.exe", "notepad.exe"};

    private final File winePrefixDirectory;
    private PrintWriter printWriterLogger = null;
    private static final Logger LOGGER = Logger.getLogger(WinePrefix.class);
    private WinePrefixLogger logOutputStream = null;

    public WinePrefix(File winePrefixDirectory) throws WineException {
        this.winePrefixDirectory = winePrefixDirectory;
    }

    private RegistryKey parseRegistryFile(String filename, String nodeName) throws WineException {
        try {
            RegistryParser registryParser = new RegistryParser(new File(winePrefixDirectory, filename), nodeName);
            return registryParser.parseFile();
        } catch (IOException e) {
            throw new UninitializedWineprefixException("The virtual drive seems to be uninitialized", e);
        } catch (ParseException e) {
            throw new UninitializedWineprefixException("The virtual drive registry files seem to be corrupted", e);
        }
    }

    /**
     * Return the system registry
     * @return the system registry
     * @throws WineException if any error happen while parsing the registry
     */
    public RegistryKey fetchSystemRegistry() throws WineException {
        return parseRegistryFile(SYSTEM_REGISTRY_FILENAME, SYSTEM_REGISTRY_NODENAME);
    }

    public RegistryKey fetchUserRegistry() throws WineException {
        return parseRegistryFile(USER_REGISTRY_FILENAME, USER_REGISTRY_NODENAME);
    }

    private File getDriveCPath() {
        return new File(winePrefixDirectory, DRIVE_C);
    }

    public String getAbsolutePath() {
        return this.winePrefixDirectory.getAbsolutePath();
    }

    public File getWinePrefixDirectory() {
        return this.winePrefixDirectory;
    }

    public long getSize() {
        try {
            return FileUtils.sizeOfDirectory(this.winePrefixDirectory);
        } catch(IllegalArgumentException e) {
            LOGGER.info("IllegalArgumentException was thrown while trying to read the directory size. Retrying...", e);
            return getSize();
        }
    }

    private Collection<File> findAllFilesByExtension(String extension) {
        return findAllFilesByExtension(extension, this.getDriveCPath());
    }

    private Collection<File> findAllFilesByExtension(String extension, File searchPath) {
        final Collection<File> candidates = new ArrayList<>();
        final File[] filesInSearchPath = searchPath.listFiles();
        assert filesInSearchPath != null;
        for(File candidate: filesInSearchPath) {
            if(candidate.isDirectory()) {
                candidates.addAll(findAllFilesByExtension(extension, candidate));
            } else if(candidate.getName().toLowerCase().endsWith(extension.toLowerCase()) &&
                    !checkSearchExcludedFiles(candidate.getName())) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    private boolean checkSearchExcludedFiles(String candidateName) {
        return (Arrays.binarySearch(SEARCH_EXCLUDED_EXECUTABLE, candidateName) == 0);
    }

    public Collection<File> findAllExecutables() {
        return findAllFilesByExtension(EXECUTABLE_EXTENSION);
    }

    public void delete() throws IOException {
        Files.remove(this.getWinePrefixDirectory());
    }

    public boolean initialized() {
        return new File(getWinePrefixDirectory(), PLAYONLINUX_WINEPREFIX_CONFIGFILE).exists();
    }

    public boolean exists() {
        return getWinePrefixDirectory().exists();
    }

    public void createConfigFile(WineDistribution wineDistribution, Version version) throws WineException {
        this.createPrefixDirectory();
        final ConfigFile prefixConfigFile = getPrefixConfigFile();

        if(initialized()) {
            throw new WineException("Prefix already exists: " + getWinePrefixDirectory());
        }

        try {
            prefixConfigFile.writeValue("distributionCode", wineDistribution.getDistributionCode());
            prefixConfigFile.writeValue("operatingSystem", wineDistribution.getOperatingSystem().name());
            prefixConfigFile.writeValue("architecture", wineDistribution.getArchitecture().name());
            prefixConfigFile.writeValue("version", version.toString());
        } catch (IOException e) {
            throw new WineException("Error while writing data on config file", e);
        }

    }


    private void createPrefixDirectory() throws WineException {
        if(!this.winePrefixDirectory.exists()) {
            if(!this.winePrefixDirectory.mkdirs()) {
                throw new WineException("Cannot createPrefix prefix: " + getWinePrefixDirectory());
            }
        }
    }

    public Version fetchVersion() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        if(prefixConfigFile.contains("version")) {
            return new Version(prefixConfigFile.readValue("version"));
        } else if (prefixConfigFile.contains("VERSION")) {
            return new Version(prefixConfigFile.readValue("VERSION"));
        }

        // FIXME: Handle this case with a default version
        throw new IllegalStateException("Prefix does not contain any version information");
    }

    public OperatingSystem fetchOperatingSystem() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        if(prefixConfigFile.contains("operatinSystem")) {
            return OperatingSystem.valueOf(prefixConfigFile.readValue("operatingSystem"));
        }

        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public Architecture fetchArchitecture() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        if(prefixConfigFile.contains("architecture")) {
            return Architecture.valueOf(prefixConfigFile.readValue("architecture"));
        } else if (prefixConfigFile.contains("ARCH")) {
            return Architecture.fromWinePackageName(prefixConfigFile.readValue("ARCH"));
        }

        // FIXME : Handle this case with a default architecture
        throw new IllegalStateException("Prefix does not contain any architecture information");
    }

    public WineDistribution fetchDistribution() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        String distribution;
        if(prefixConfigFile.contains("distributionCode")) {
            distribution = prefixConfigFile.readValue("distributionCode");
        } else {
            distribution = "upstream";
        }

        return new WineDistribution(fetchOperatingSystem(), fetchArchitecture(), distribution);
    }

    public ConfigFile getPrefixConfigFile() {
        return new CompatibleConfigFileFormat(new File(getWinePrefixDirectory(), PLAYONLINUX_WINEPREFIX_CONFIGFILE));
    }

    public void log(String message) throws IOException {
        if(printWriterLogger == null) {
            logOutputStream = loggerFactory.getWinePrefixLogger(this.fetchName());
            printWriterLogger = new PrintWriter(logOutputStream);
            printWriterLogger.println(message);
            printWriterLogger.flush();
        }
    }

    private String fetchName() {
        return this.getWinePrefixDirectory().getName();
    }

    @Override
    public void close()  {
        if(printWriterLogger != null) {
            printWriterLogger.flush();
            printWriterLogger.close();
            try {
                loggerFactory.close(logOutputStream);
            } catch (IOException e) {
                LOGGER.warn("The log stream could not be closed");
            }
        }
    }
}
