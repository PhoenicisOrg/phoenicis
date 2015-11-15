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

package com.playonlinux.win32.pe.rsrc;


public class RsrcSection {
    public final ImageResourceDirectory imageResourceDirectory;
    public final ImageResourceDirectoryEntry[] imageResourceDirectoryEntry;
    public RsrcSection(byte[] bytes) {
        imageResourceDirectory = new ImageResourceDirectory(bytes);
        imageResourceDirectoryEntry = new ImageResourceDirectoryEntry[
                imageResourceDirectory.numberOfNamedEntries.getUnsignedValue() +
                        imageResourceDirectory.numberOfIdEntries.getUnsignedValue()
                ];

        int i = 0;
        for(; i < imageResourceDirectory.numberOfNamedEntries.getUnsignedValue(); i++) {
            imageResourceDirectoryEntry[i] = new ImageResourceNamedDirectoryEntry(bytes,
                    ImageResourceDirectory.IMAGE_RESOURCE_DIRECTORY_SIZE);
        }

        for(; i < imageResourceDirectory.numberOfIdEntries.getUnsignedValue(); i++) {
            imageResourceDirectoryEntry[i] = new ImageResourceDirectoryEntry(bytes,
                    ImageResourceDirectory.IMAGE_RESOURCE_DIRECTORY_SIZE);
        }
    }
}
