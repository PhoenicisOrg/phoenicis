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

package com.playonlinux.core.utils.archive.cab;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class CFData extends AbstractCabStructure {
    private final CompressionType compressionType;
    byte[] csum = new byte[4];
    byte[] cbData = new byte[2];
    byte[] cbUncomp = new byte[2];

    byte[] abReserve = new byte[256];
    byte[] ab;

    CFData(long offset, CompressionType compressionType) {
        super(offset);
        this.compressionType = compressionType;
    }

    @Override
    public void populate(InputStream inputStream) throws CabException {
        try {
            structureSize += inputStream.read(csum);
            structureSize += inputStream.read(cbData);
            structureSize += inputStream.read(cbUncomp);

            // structureSize += readVariableField(inputStream, abReserve);

            ab = new byte[(int) getCompressedSize()];

            structureSize += inputStream.read(ab);

        } catch (IOException e) {
            throw new CabException("Unable to extract CFFolder", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Checksum: %s\n" + "Offset: %s\n" + "Compressed size: %s\n" + "Uncompressed size: %s\n",
                Arrays.toString(csum), offset, getCompressedSize(), getUncompressedSize());
    }

    public long getCompressedSize() {
        return this.decodeLittleEndian(cbData);
    }

    public long getUncompressedSize() {
        return this.decodeLittleEndian(cbUncomp);
    }

}