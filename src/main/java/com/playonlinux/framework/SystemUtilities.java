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

package com.playonlinux.framework;

import com.playonlinux.domain.ScriptClass;

import java.io.File;

@ScriptClass
@SuppressWarnings("unused")
public class SystemUtilities {
    final static int KILOBYTE = 1024;
    // TODO: Find a name that do not use the verb "get"

    public static long getFreeSpace(File directory) {
        return directory.getUsableSpace() / KILOBYTE;
    }

    public static long getFreeSpace(String directory) {
        return getFreeSpace(new File(directory));
    }
}
