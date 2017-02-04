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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class CompatibleConfigFileFormatFactory {
    private final ObjectMapper objectMapper;

    public CompatibleConfigFileFormatFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ConfigFile open(String filePath) {
        return new CompatibleConfigFileFormat(objectMapper, new File(filePath));
    }

    public ConfigFile open(File file) {
        return new CompatibleConfigFileFormat(objectMapper, file);
    }
}
