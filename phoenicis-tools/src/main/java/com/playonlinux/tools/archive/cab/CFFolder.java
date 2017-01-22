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

package com.playonlinux.tools.archive.cab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class CFFolder extends AbstractCabStructure {
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractCabStructure.class);

    byte[] coffCabStart = new byte[4];
    byte[] cCFData = new byte[2];
    byte[] typeCompress = new byte[2];

    byte[] abReserve = new byte[256];

    CFFolder(long offset) {
        super(offset);
    }


    @Override
    public void populate(InputStream inputStream) {
        try {
            structureSize += inputStream.read(coffCabStart);
            structureSize += inputStream.read(cCFData);
            structureSize += inputStream.read(typeCompress);

            //structureSize += readVariableField(inputStream, abReserve);
        } catch (IOException e) {
            throw new CabException("Unable to extract CFFolder", e);
        }

    }

    public long getOffsetStartData() {
        return decodeLittleEndian(coffCabStart);
    }

    public long getNumberOfDataStructures() {
        return decodeLittleEndian(cCFData);
    }

    public CompressionType getCompressType() {
        Long compressType = decodeLittleEndian(typeCompress) & 0x000F;
        if(compressType == 0) {
            return CompressionType.NONE;
        }
        if(compressType == 1) {
            return CompressionType.MSZIP;
        }
        if(compressType == 2) {
            return CompressionType.QUANTUM;
        }
        if(compressType == 3) {
            return CompressionType.LZX;
        }
        throw new CabException("Unsupported compression type");
    }

    @Override
    public String toString() {
        String compressType;
        try {
            compressType = getCompressType().name();
        } catch (CabException e) {
            LOGGER.warn("Failed to find compress type", e);
            compressType = "Unknown";
        }

        return String.format(
                "Offset: %s\n" +
                "Size: %s\n" +
                "Offset of the first data: %s\n" +
                "Number of data structures: %s\n" +
                "typeCompress: %s\n",
                offset,
                getStructureSize(),
                getOffsetStartData(),
                getNumberOfDataStructures(),
                compressType
        );
    }

}