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

package org.phoenicis.tools.archive.cab;

import java.io.*;

public class Cabfile {
    private final InputStream archiveStream;
    private long readBytes = 0;

    public Cabfile(File archiveFile) {
        try {
            this.archiveStream = new FileInputStream(archiveFile);
        } catch (FileNotFoundException e) {
            throw new CabException(String.format("The file %s is not found", archiveFile), e);
        }
    }

    private int findCabOffset() {
        try {
            int i = 0;
            int successBytes = 0;
            while (true) {
                byte[] nextRead = new byte[1];
                int numRead = archiveStream.read(nextRead);

                if (numRead == -1) {
                    throw new CabException("This file does not contain any cabinet archive");
                }

                if ((nextRead[0] & 0xFF) == 0x4D && successBytes == 0) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x53 && successBytes == 1) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x43 && successBytes == 2) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x46 && successBytes == 3) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x00 && successBytes == 4) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x00 && successBytes == 5) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x00 && successBytes == 6) {
                    successBytes++;
                } else if ((nextRead[0] & 0xFF) == 0x00 && successBytes == 7) {
                    return i - 7;
                } else {
                    successBytes = 0;
                }
                i++;
            }
        } catch (IOException e) {
            throw new CabException("Unable to find cab header", e);
        }
    }

    private CFHeader getHeader() {
        CFHeader cfHeader = new CFHeader(readBytes);
        cfHeader.populate(archiveStream);
        readBytes += (long) cfHeader.getStructureSize();

        return cfHeader;
    }

    public CFFolder getFolder() {
        CFFolder cfFolder = new CFFolder(readBytes);
        cfFolder.populate(archiveStream);
        readBytes += (long) cfFolder.getStructureSize();
        return cfFolder;
    }

    public CFFile getFile() {
        CFFile cfFile = new CFFile(readBytes);
        cfFile.populate(archiveStream);
        readBytes += (long) cfFile.getStructureSize();
        return cfFile;
    }

    public CFData getData(CompressionType compressionType) {
        CFData cfData = new CFData(readBytes, compressionType);
        cfData.populate(archiveStream);
        readBytes += (long) cfData.getStructureSize();
        return cfData;
    }

    private void skipBytes(long numberToSkip) throws IOException {
        readBytes += numberToSkip;
        archiveStream.skip(numberToSkip);
    }

    private void jumpTo(long offset) throws IOException {
        this.skipBytes(offset - readBytes);
    }
}
