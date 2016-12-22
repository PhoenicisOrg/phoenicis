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

package com.playonlinux.scripts.framework;


import java.io.File;

public final class SystemUtilities {
    static final long KILOBYTE = 1024L;

    private SystemUtilities() {
        // Utility class
    }

    public static long getFreeSpace(File directory) {
        return directory.getUsableSpace() / KILOBYTE;
    }

    public static long getFreeSpace(String directory) {
        return getFreeSpace(new File(directory));
    }
}
