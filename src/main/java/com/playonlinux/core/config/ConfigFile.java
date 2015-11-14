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

import java.io.IOException;

/**
 * Represents a config file. A config file is a set of (Key, Value) string pairs
 */
public interface ConfigFile {
    /**
     * Write a value in the config file
     *
     * @param key
     *            The key
     * @param value
     *            The value
     * @throws IOException
     *             If the file cannot be accessed
     */
    void writeValue(String key, String value) throws IOException;

    /**
     * Read the value of the given key
     * 
     * @param key
     *            the key to look after
     * @return the value, or null of the value cannot be found
     */
    String readValue(String key);

    /**
     * Test if the config files contains the given key
     * 
     * @param key
     *            The key to test
     * @return true if the config file contains the given key. false in any
     *         other cases
     */
    boolean contains(String key);

    /**
     * Read the value, but provide a default value in the case the value is not
     * found
     * 
     * @param key
     *            the key to look after
     * @param defaultValue
     *            the default value to return if the config file does not
     *            contain the given key
     * @return the value, or the default value if the value cannot be found
     */
    String readValue(String key, String defaultValue);

    /**
     * Delete a (key, value) pair from the config file
     * 
     * @param key
     *            the key to delete
     * @throws IOException
     *             If the file cannot be accessed
     */
    void deleteValue(String key) throws IOException;
}
