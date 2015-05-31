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

package com.playonlinux.utils.cab;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class CFHeader extends AbstractCabStructure {
    byte[] signature = { 'M', 'S', 'C', 'F' };
    byte[] reserved1 = { '\0', '\0', '\0', '\0' };
    byte[] cbCabinet = new byte[4];
    byte[] reserved2 = new byte[4];
    byte[] coffFiles = new byte[4];
    byte[] reserved3 = new byte[4];
    byte[] versionMinor = new byte[1];
    byte[] versionMajor = new byte[1];
    byte[] cFolders = new byte[2];
    byte[] cFiles = new byte[2];
    byte[] flags = new byte[2];
    byte[] setID = new byte[2];
    byte[] iCabinet = new byte[2];

    byte[] cbCFHeader = new byte[2];
    byte[] cbCFFolder = new byte[1];
    byte[] cbCFData = new byte[1];

    byte[] abReserve = new byte[255];
    byte[] szCabinetPrev = new byte[255];
    byte[] szDiskPrev = new byte[255];
    byte[] szCabinetNext = new byte[255];
    byte[] szDiskNext = new byte[255];

    CFHeader(long offset) {
        super(offset);
    }


    public void populate(InputStream inputStream) throws CabException {
        try {
            structureSize += 8;
            structureSize += inputStream.read(cbCabinet);
            structureSize += inputStream.read(reserved2);
            structureSize += inputStream.read(coffFiles);
            structureSize += inputStream.read(reserved3);
            structureSize += inputStream.read(versionMinor);
            structureSize += inputStream.read(versionMajor);
            structureSize += inputStream.read(cFolders);
            structureSize += inputStream.read(cFiles);
            structureSize += inputStream.read(flags);
            structureSize += inputStream.read(setID);
            structureSize += inputStream.read(iCabinet);


            /* FIXME: Read the flag
            structureSize += inputStream.read(cbCFHeader);
            structureSize += inputStream.read(cbCFFolder);
            structureSize += inputStream.read(cbCFData);
            */

            //structureSize += readVariableField(inputStream, abReserve);
            //structureSize += readVariableField(inputStream, szCabinetPrev);
            //structureSize += readVariableField(inputStream, szDiskPrev);
            //structureSize += readVariableField(inputStream, szCabinetNext);
            //structureSize += readVariableField(inputStream, szDiskNext);
        } catch (IOException e) {
            throw new CabException("Unable to parse header", e);
        }
    }


    public int getNumberOfFiles() {
        // Maybe it needs to be checked
        return (cFiles[0] & 0xFF) + (cFiles[1] & 0xFF) * 16;
    }

    public int getNumberOfFolders() {
        // Maybe it needs to be checked
        return (cFolders[0] & 0xFF) + (cFolders[1] & 0xFF) * 16;
    }


    public String toString() {
        return String.format(
                "Offset: %s\n" +
                "Signature: %s\n" +
                "Reserved1: %s\n" +
                "cbCabinet: %s\n" +
                "Reserved2: %s\n" +
                "coffFiles: %s\n" +
                "Reserved3: %s\n" +
                "VersionMinor: %s\n" +
                "VersionMajor: %s\n" +
                "cFolders: %s\n" +
                "cFiles: %s\n" +
                "cbCFData: %s\n" +
                "cbCFFolder: %s",
                offset,
                Arrays.toString(signature),
                Arrays.toString(reserved1),
                Arrays.toString(cbCabinet),
                Arrays.toString(reserved2),
                Arrays.toString(coffFiles),
                Arrays.toString(reserved3),
                versionMinor[0],
                versionMajor[0],
                Arrays.toString(cFolders),
                Arrays.toString(cFiles),
                cbCFData[0],
                cbCFFolder[0]
        );
    }
}
