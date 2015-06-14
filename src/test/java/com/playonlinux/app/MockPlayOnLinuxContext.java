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

import com.playonlinux.utils.ReplacableProperties;

import java.io.IOException;

public class MockPlayOnLinuxContext extends PlayOnLinuxContext {

    public MockPlayOnLinuxContext() throws PlayOnLinuxException, IOException {
        super();
    }

    public ReplacableProperties loadProperties() {
        ReplacableProperties propertiesBeingLoaded = new ReplacableProperties();

        try {
            propertiesBeingLoaded.load(PlayOnLinuxContext.class.getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException e) {
            throw new PlayOnLinuxRuntimeError("Cannot load properties", e);
        }
        return propertiesBeingLoaded;
    }

}
