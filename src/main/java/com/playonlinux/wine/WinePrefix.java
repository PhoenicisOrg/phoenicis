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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class WinePrefix {
    private static final String PLAYONLINUX_WINEPREFIX_CONFIGFILE = "playonlinux.cfg";
    private static final String SYSTEM_REGISTRY_FILENAME = "system.reg";
    private static final String SYSTEM_REGISTRY_NODENAME = "HKEY_LOCAL_MACHINE";
    private static final String USER_REGISTRY_FILENAME = "user.reg";
    private static final String USER_REGISTRY_NODENAME = "HKEY_CURRENT_USER";


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

}
