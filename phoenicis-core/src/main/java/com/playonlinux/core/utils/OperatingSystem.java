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

package com.playonlinux.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a supported Operating System
 */
@RequiredArgsConstructor
public enum OperatingSystem {
    MACOSX("Mac OS X", "Mac", "darwin"), LINUX("Linux", "Linux", "linux"), FREEBSD("FreeBSD", "FreeBSD", "freebsd");

    // PlayOnLinux v4 compatible short name
    @Getter
    private final String fullName;
    // PlayOnLinux v4 compatible short name
    @Getter
    private final String legacyName;
    // The name used for the wine packages
    @Getter
    private final String winePackage;

    /**
     * Creates a Operating System from the name
     *
     * @param fullName
     *            The name
     * @return The operating system
     */
    public static OperatingSystem fromString(String fullName) {
        for (OperatingSystem system : OperatingSystem.values()) {
            if (system.getFullName().equals(fullName)) {
                return system;
            }
        }

        throw new IllegalArgumentException(String.format("Incompatible operation system \"%s\"", fullName));
    }

    /**
     * Fetch the current Operating System
     *
     * @return The current operating system
     */
    public static OperatingSystem fetchCurrentOperationSystem() {
        return OperatingSystem.fromString(System.getProperty("os.name"));
    }

    @Override
    public String toString() {
        return this.fullName;
    }
}
