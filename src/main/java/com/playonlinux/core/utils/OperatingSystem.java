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

/**
 * Represents a supported Operating System
 */
public enum OperatingSystem {
    MACOSX ("Mac OS X"),
    LINUX ("Linux"),
    FREEBSD ("FreeBSD");

    private final String name;

    OperatingSystem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Creates a Operating System from the name
     * @param name The name
     * @return The operating system
     */
    public static OperatingSystem fromString(String name) {
        switch(name) {
            case "Mac OS X":
                return MACOSX;
            case "Linux":
                return LINUX;
            case "FreeBSD":
                return FREEBSD;
        }

        throw new IllegalArgumentException(String.format("Incompatible operation system \"%s\"", name));
    }

    public String fetchShortName() {
        return this.name();
    }

    /**
     *
     * @return PlayOnLinux v4 compatible short name
     */
    public String fetchLegacyName() {
        switch (this) {
            case FREEBSD:
                return "FreeBSD";
            case MACOSX:
                return "Mac";
            case LINUX:
            default:
                return "Linux";
        }
    }

    /**
     *
     * @return The name used for the wine packages
     */
    public String getNameForWinePackages() {
        switch (this) {
            case FREEBSD:
                return "freebsd";
            case MACOSX:
                return "darwin";
            case LINUX:
            default:
                return "linux";
        }
    }

    /**
     * Fetch the current Operating System
     * @return The current operating system
     */
    public static OperatingSystem fetchCurrentOperationSystem() {
        return OperatingSystem.fromString(System.getProperty("os.name"));
    }


}
