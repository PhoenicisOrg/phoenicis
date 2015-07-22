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

package com.playonlinux.utils.archive.cab;

import java.io.IOException;
import java.io.InputStream;

public class CFFile extends AbstractCabStructure {
    byte[] cbFile = new byte[4];
    byte[] uoffFolderStart = new byte[4];
    byte[] iFolder = new byte[2];
    byte[] date = new byte[2];
    byte[] time = new byte[2];
    byte[] attribs = new byte[2];
    byte[] szName = new byte[257];

    CFFile(long offset) {
        super(offset);
    }

    @Override
    public void populate(InputStream inputStream) throws CabException {
        try {
            structureSize += inputStream.read(cbFile);
            structureSize += inputStream.read(uoffFolderStart);
            structureSize += inputStream.read(iFolder);
            structureSize += inputStream.read(date);
            structureSize += inputStream.read(time);
            structureSize += inputStream.read(attribs);

            structureSize += readVariableField(inputStream, szName);
        } catch (IOException e) {
            throw new CabException("Unable to extract CFFolder", e);
        }

    }

    public long getUncompressedSize() {
        return decodeLittleEndian(cbFile);
    }

    public long getOffsetStartDataInsideFolder() {
        return decodeLittleEndian(uoffFolderStart);
    }

    public long getFolderIndex() {
        return decodeLittleEndian(iFolder);
    }

    public String toString() {
        return String.format(
                "Offset: %s\n" +
                "Size: %s\n" +
                "Uncompressed size: %s\n" +
                "Data offset: %s\n" +
                "Folder: %s\n" +
                "szName: %s\n",

                offset,
                getStructureSize(),
                this.getUncompressedSize(),
                this.getOffsetStartDataInsideFolder(),
                this.getFolderIndex(),
                this.getFilename()
        );
    }

    public String getFilename() {
        return new String(szName).split("\0")[0];
    }
}