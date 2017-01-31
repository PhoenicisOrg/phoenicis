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

package com.playonlinux.tools.system;

import com.phoenicis.entities.Architecture;
import com.phoenicis.entities.OperatingSystem;

public class ArchitectureFetcher {
    private final OperatingSystemFetcher operatingSystemFetcher;

    public ArchitectureFetcher(OperatingSystemFetcher operatingSystemFetcher) {
        this.operatingSystemFetcher = operatingSystemFetcher;
    }

    /**
     * Find the current architecture
     * @return The current architecture
     */
    public Architecture fetchCurrentArchitecture() {
        if(operatingSystemFetcher.fetchCurrentOperationSystem() == OperatingSystem.MACOSX) {
            return Architecture.AMD64;
        }
        if("x86".equals(System.getProperty("os.arch"))) {
            return Architecture.I386;
        } else {
            return Architecture.AMD64;
        }
    }

    public Architecture fromWinePackageName(String architectureName) {
        switch (architectureName) {
            case "x86":
                return Architecture.I386;
            case "amd64":
                return Architecture.AMD64;
            default:
                throw new IllegalArgumentException(String.format("Unknown architecture '%s'", architectureName));
        }
    }

}
