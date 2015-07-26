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

package com.playonlinux.core.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;

/**
 * Represents a PlayOnLinux config file.
 * The class is able to read POLv4 format, but it will store everything in v5 format (json)
 */
public class CompatibleConfigFileFormat implements ConfigFile {

    private static final Logger LOGGER  = Logger.getLogger(CompatibleConfigFileFormat.class);
    private final File configFile;

    public CompatibleConfigFileFormat(File configFile) {
        this.configFile = configFile;
    }

    @Override
    public synchronized void writeValue(String key, String value) {

    }

    @Override
    public String readValue(String key) {
        return readValue(key, "");
    }

    @Override
    public synchronized String readValue(String key, String defaultValue) {
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> map;
        try {
            map = mapper.readValue(configFile, Map.class);
        } catch (JsonParseException | JsonMappingException e) {
            LOGGER.info("The file does not seems to be a JSON format. Trying legacy PlayOnLinux config file", e);
            return readLegacyValue(key, defaultValue);
        } catch (IOException e) {
            LOGGER.warn("IOException while reading the config file. Assuming default value", e);
            return defaultValue;
        }
        final String value = (String) map.get(key);
        return value != null ? value : defaultValue;
    }

    private String readLegacyValue(String key, String defaultValue) {
        try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))) {
            for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] splitLine = line.split("=");

                if(splitLine[0].equals(key)) {
                    StringBuilder result = new StringBuilder();
                    for(int i = 1; i < splitLine.length; i++) {
                        if(i != 1) {
                            result.append('=');
                        }
                        result.append(splitLine[i]);
                    }
                    return result.toString();
                }
            }
            return defaultValue;
        } catch (IOException e) {
            LOGGER.warn("IOException while reading the config file. Assuming default value", e);
            return defaultValue;
        }
    }

    @Override
    public synchronized void deleteValue(String key) {

    }
}
