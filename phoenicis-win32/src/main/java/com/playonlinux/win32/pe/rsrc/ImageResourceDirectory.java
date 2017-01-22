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

package com.playonlinux.win32.pe.rsrc;

import com.playonlinux.win32.ULong;
import com.playonlinux.win32.UShort;

public class ImageResourceDirectory {
    public static final int IMAGE_RESOURCE_DIRECTORY_SIZE = 16;

    public final ULong characteristics;
    public final ULong timeDateStamp;
    public final UShort majorVersion;
    public final UShort minorVersion;
    public final UShort numberOfNamedEntries;
    public final UShort numberOfIdEntries;

    public ImageResourceDirectory(byte[] bytes) {
        characteristics = new ULong(bytes, 0);
        timeDateStamp = new ULong(bytes, 4);
        majorVersion = new UShort(bytes, 8);
        minorVersion = new UShort(bytes, 10);
        numberOfNamedEntries = new UShort(bytes, 12);
        numberOfIdEntries = new UShort(bytes, 14);
    }
}
