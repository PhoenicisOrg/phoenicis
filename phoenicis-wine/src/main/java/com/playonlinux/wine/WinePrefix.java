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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.playonlinux.core.config.CompatibleConfigFileFormat;
import com.playonlinux.core.config.ConfigFile;
import com.playonlinux.core.log.WinePrefixLogger;
import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.Files;
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.core.version.Version;
import com.playonlinux.engines.wine.WineDistribution;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.wine.configurations.DefaultWinePrefixDisplayConfiguration;
import com.playonlinux.wine.configurations.DefaultWinePrefixInputConfiguration;
import com.playonlinux.wine.configurations.RegistryWinePrefixDisplayConfiguration;
import com.playonlinux.wine.configurations.RegistryWinePrefixInputConfiguration;
import com.playonlinux.wine.configurations.WinePrefixDisplayConfiguration;
import com.playonlinux.wine.configurations.WinePrefixInputConfiguration;
import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryParser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a wineprefix
 */
@Slf4j
@RequiredArgsConstructor
@Scan
public class WinePrefix implements AutoCloseable {
    private static final Version DEFAULT_VERSION = new Version("1.7.49"); // FIXME

    @Inject
    static com.playonlinux.core.log.LoggerFactory loggerFactory;

    private static final String ARCHITECTURE = "architecture";
    private static final String CONTAINER_TYPE = "containerType";
    private static final String DISTRIBUTION_CODE = "distributionCode";
    private static final String DRIVE_C = "drive_c";
    private static final String EXECUTABLE_EXTENSION = "exe";
    private static final String OPERATING_SYSTEM = "operatingSystem";
    private static final String USER_REGISTRY_FILENAME = "user.reg";
    private static final String USER_REGISTRY_NODENAME = "HKEY_CURRENT_USER";
    private static final String PLAYONLINUX_WINEPREFIX_CONFIGFILE = "playonlinux.cfg";
    private static final String SYSTEM_REGISTRY_FILENAME = "system.reg";
    private static final String SYSTEM_REGISTRY_NODENAME = "HKEY_LOCAL_MACHINE";
    private static final String VERSION_LC = "version";
    private static final String VERSION_UC = "VERSION";

    private static final String[] SEARCH_EXCLUDED_EXECUTABLE = new String[] { "iexplore.exe", "notepad.exe" };

    @Getter
    private final File winePrefixDirectory;
    private PrintWriter printWriterLogger = null;
    private WinePrefixLogger logOutputStream = null;

    private RegistryKey parseRegistryFile(String filename, String nodeName) throws WineException {
        RegistryParser registryParser = new RegistryParser(new File(winePrefixDirectory, filename), nodeName);
        return registryParser.parseFile();
    }

    /**
     * Return the system registry
     *
     * @return the system registry
     * @throws WineException
     *             if any error happen while parsing the registry
     */
    public RegistryKey fetchSystemRegistry() throws WineException {
        return parseRegistryFile(SYSTEM_REGISTRY_FILENAME, SYSTEM_REGISTRY_NODENAME);
    }

    public RegistryKey fetchUserRegistry() throws WineException {
        return parseRegistryFile(USER_REGISTRY_FILENAME, USER_REGISTRY_NODENAME);
    }

    public File getDriveCPath() {
        return new File(winePrefixDirectory, DRIVE_C);
    }

    public String getAbsolutePath() {
        return winePrefixDirectory.getAbsolutePath();
    }

    public long getSize() {
        try {
            return FileUtils.sizeOfDirectory(this.winePrefixDirectory);
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException was thrown while trying to read the directory size. Retrying...", e);
            return getSize();
        }
    }

    private Collection<File> findAllFilesByExtension(String extension) {
        return findAllFilesByExtension(extension, this.getDriveCPath());
    }

    private Collection<File> findAllFilesByExtension(String extension, File searchPath) {
        final Collection<File> candidates = new ArrayList<>();
        final File[] filesInSearchPath = searchPath.listFiles();

        for (File candidate : filesInSearchPath) {
            if (candidate.isDirectory() && !java.nio.file.Files.isSymbolicLink(candidate.toPath())) {
                candidates.addAll(findAllFilesByExtension(extension, candidate));
            } else if (candidate.getName().toLowerCase().endsWith(extension.toLowerCase())
                    && !checkSearchExcludedFiles(candidate.getName())) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    private boolean checkSearchExcludedFiles(String candidateName) {
        return Arrays.binarySearch(SEARCH_EXCLUDED_EXECUTABLE, candidateName) == 0;
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

        if (initialized()) {
            throw new WineException("Prefix already exists: " + getWinePrefixDirectory());
        }

        try {
            prefixConfigFile.writeValue(CONTAINER_TYPE, "WinePrefixContainer");
            prefixConfigFile.writeValue(DISTRIBUTION_CODE, wineDistribution.getDistributionCode());
            prefixConfigFile.writeValue(OPERATING_SYSTEM, wineDistribution.getOperatingSystem().name());
            prefixConfigFile.writeValue(ARCHITECTURE, wineDistribution.getArchitecture().name());
            prefixConfigFile.writeValue(VERSION_LC, version.toString());
        } catch (IOException e) {
            throw new WineException("Error while writing data on config file", e);
        }

    }

    private void createPrefixDirectory() throws WineException {
        if (!this.winePrefixDirectory.exists() && !this.winePrefixDirectory.mkdirs()) {
            throw new WineException("Cannot createPrefix prefix: " + getWinePrefixDirectory());
        }
    }

    public Version fetchVersion() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        if (prefixConfigFile.contains(VERSION_LC)) {
            return new Version(prefixConfigFile.readValue(VERSION_LC));
        } else if (prefixConfigFile.contains(VERSION_UC)) {
            return new Version(prefixConfigFile.readValue(VERSION_UC));
        }

        // FIXME: Handle this case with a default version
        return DEFAULT_VERSION;
    }

    public boolean isAutomaticallyUpdated() {
        return getPrefixConfigFile().contains(VERSION_LC) || getPrefixConfigFile().contains(VERSION_UC);
    }

    public OperatingSystem fetchOperatingSystem() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        if (prefixConfigFile.contains("operatinSystem")) {
            return OperatingSystem.valueOf(prefixConfigFile.readValue(OPERATING_SYSTEM));
        }

        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public Architecture fetchArchitecture() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        if (prefixConfigFile.contains(ARCHITECTURE)) {
            return Architecture.valueOf(prefixConfigFile.readValue(ARCHITECTURE));
        } else if (prefixConfigFile.contains("ARCH")) {
            return Architecture.fromWinePackageName(prefixConfigFile.readValue("ARCH"));
        }

        // FIXME : Fix the config file
        return Architecture.I386;
    }

    public WineDistribution fetchDistribution() {
        final ConfigFile prefixConfigFile = getPrefixConfigFile();
        final String distribution;
        if (prefixConfigFile.contains(DISTRIBUTION_CODE)) {
            distribution = prefixConfigFile.readValue(DISTRIBUTION_CODE);
        } else {
            distribution = "upstream";
        }

        return new WineDistribution(fetchOperatingSystem(), fetchArchitecture(), distribution);
    }

    public ConfigFile getPrefixConfigFile() {
        return new CompatibleConfigFileFormat(new File(getWinePrefixDirectory(), PLAYONLINUX_WINEPREFIX_CONFIGFILE));
    }

    public void log(String message) throws IOException {
        if (printWriterLogger == null) {
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
    public void close() {
        if (printWriterLogger != null) {
            printWriterLogger.flush();
            printWriterLogger.close();
            try {
                loggerFactory.close(logOutputStream);
            } catch (IOException e) {
                log.warn("The log stream could not be closed", e);
            }
        }
    }

    public WinePrefixDisplayConfiguration getDisplayConfiguration() {
        try {
            return new RegistryWinePrefixDisplayConfiguration(fetchUserRegistry());
        } catch (WineException e) {
            log.debug("Failed to get display configuration", e);
            return new DefaultWinePrefixDisplayConfiguration();
        }
    }

    public WinePrefixInputConfiguration getInputConfiguration() {
        try {
            return new RegistryWinePrefixInputConfiguration(fetchUserRegistry());
        } catch (WineException e) {
            log.debug("Failed to get input configuration", e);
            return new DefaultWinePrefixInputConfiguration();
        }
    }
}
