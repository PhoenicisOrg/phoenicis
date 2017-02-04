/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.tools.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a Phoenicis config file. The class is able to read POLv4 format,
 * but it will store everything in v5 format (json)
 */
public class CompatibleConfigFileFormat implements ConfigFile {
    private final Logger LOGGER  = LoggerFactory.getLogger(CompatibleConfigFileFormat.class);
    private final File configFile;
    private final ObjectMapper mapper;

    public CompatibleConfigFileFormat(ObjectMapper objectMapper, File configFile) {
        this.configFile = configFile;
        this.mapper = objectMapper;
    }

    @Override
    public synchronized void writeValue(String key, String value) throws IOException {
        final Map<String, String> values = this.getMap();
        values.put(key, value);
        mapper.writeValue(configFile, values);
    }

    @Override
    public String readValue(String key) {
        return readValue(key, "");
    }

    @Override
    public boolean contains(String key) {
        return this.getMap().containsKey(key);
    }

    @Override
    public synchronized String readValue(String key, String defaultValue) {
        final String value = this.getMap().get(key);
        return value != null ? value : defaultValue;
    }

    private Map<String, String> getMap() {
        try {
            final Map<String, String> results = new HashMap<>();
            final Map<?, ?> tmpResults = mapper.readValue(configFile, Map.class);
            for (Entry<?, ?> entry : tmpResults.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                    results.put((String) entry.getKey(), (String) entry.getValue());
                }
            }
            return results;
        } catch (JsonParseException | JsonMappingException e) {
            LOGGER.debug("The file does not seems to be a JSON format. Trying legacy Phoenicis config file", e);
            return getLegacyMap();
        } catch (IOException e) {
            LOGGER.debug("Error while reading the file. Will assume that the config file is EMPTY", e);
            return new HashMap<>();
        }
    }

    private Map<String, String> getLegacyMap() {
        final Map<String, String> result = new HashMap<>();
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))) {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                final String[] splitLine = line.split("=");

                final String newKey = splitLine[0];
                final StringBuilder newValueBuilder = new StringBuilder();
                for (int i = 1; i < splitLine.length; i++) {
                    if (i != 1) {
                        newValueBuilder.append('=');
                    }
                    newValueBuilder.append(splitLine[i]);
                }
                final String newValue = newValueBuilder.toString();
                result.put(newKey, newValue);
            }
        } catch (IOException e) {
            LOGGER.warn("IOException while reading the config file. Assuming default value", e);
        }

        return result;
    }

    @Override
    public synchronized void deleteValue(String key) throws IOException {
        final Map<String, String> values = this.getMap();
        values.remove(key);
        mapper.writeValue(configFile, values);
    }

}
