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

import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryParser;

import org.apache.commons.io.FileUtils;
import org.python.antlr.ast.Str;

import javax.print.attribute.SetOfIntegerSyntax;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WinePrefix {
    private static final String PLAYONLINUX_WINEPREFIX_CONFIGFILE = "playonlinux.cfg";
    private static final String SYSTEM_REGISTRY_FILENAME = "system.reg";
    private static final String SYSTEM_REGISTRY_NODENAME = "HKEY_LOCAL_MACHINE";
    private static final String USER_REGISTRY_FILENAME = "user.reg";
    private static final String USER_REGISTRY_NODENAME = "HKEY_CURRENT_USER";
    private static final String EXECUTABLE_EXTENSION = "exe";
    private static final String DRIVE_C = "drive_c";

    private static String[] EXCLUDES_FILES = new String[] {"iexplore.exe", "notepad.exe"};

    private final File winePrefixDirectory;

    public WinePrefix(File winePrefixDirectory) {
        this.winePrefixDirectory = winePrefixDirectory;
        if(!this.winePrefixDirectory.exists()) {
            this.winePrefixDirectory.mkdirs();
        }
    }

    private RegistryKey parseRegistryFile(String filename, String nodeName) throws WineException {
        try {
            RegistryParser registryParser = new RegistryParser(new File(winePrefixDirectory, filename), nodeName);
            return registryParser.parseFile();
        } catch (IOException e) {
            throw new UninitializedWineprefixException("The virtual drive seems to be uninitialized");
        } catch (ParseException e) {
            throw new UninitializedWineprefixException("The virtual drive registry files seem to be corrupted");
        }
    }

    public RegistryKey fetchSystemRegistry() throws WineException {
        return parseRegistryFile(SYSTEM_REGISTRY_FILENAME, SYSTEM_REGISTRY_NODENAME);
    }

    public RegistryKey fetchUserRegistry() throws WineException {
        return parseRegistryFile(USER_REGISTRY_FILENAME, USER_REGISTRY_NODENAME);
    }

    private File getDriveCPath() {
        return new File(winePrefixDirectory, DRIVE_C);
    }

    public String fetchVersion() {
        return null;
    }

    public String fetchArchitecture() {
        return null;
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
            return getSize();
        }
    }

    private Collection<File> findAllFilesByExtension(String extension) {
        return findAllFilesByExtension(extension, this.getDriveCPath());
    }

    private Collection<File> findAllFilesByExtension(String extension, File searchPath) {
        final Collection<File> candidates = new ArrayList<>();
        final File[] filesInSearchPath = searchPath.listFiles();
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
        int isIn = Arrays.binarySearch(EXCLUDES_FILES, candidateName);
        return (isIn == 0);
    }

    public Collection<File> findAllExecutables() {
        return findAllFilesByExtension(EXECUTABLE_EXTENSION);
    }
}
