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
 * Represents an architecture
 */
public enum Architecture {
    I386, AMD64;

    /**
     * Find the current architecture
     * 
     * @return The current architecture
     */
    public static Architecture fetchCurrentArchitecture() {
        if (OperatingSystem.fetchCurrentOperationSystem() == OperatingSystem.MACOSX) {
            return I386;
        }
        if ("x86".equals(System.getProperty("os.arch"))) {
            return I386;
        } else {
            return AMD64;
        }
    }

    public static Architecture fromWinePackageName(String architectureName) {
        switch (architectureName) {
            case "x86":
                return I386;
            case "amd64":
                return AMD64;
            default:
                throw new IllegalArgumentException(String.format("Unknown architecture '%s'", architectureName));
        }
    }

    public String getNameForWinePackages() {
        switch (this) {
            case AMD64:
                return "amd64";
            case I386:
            default:
                return "x86";
        }
    }
}
