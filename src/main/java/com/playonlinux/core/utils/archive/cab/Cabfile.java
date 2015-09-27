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

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class Cabfile {
    private final InputStream archiveStream;
    private long readBytes = 0;

    public Cabfile(File archiveFile) throws CabException {
        try {
            this.archiveStream = new FileInputStream(archiveFile);
        } catch (FileNotFoundException e) {
            throw new CabException(String.format("The file %s is not found", archiveFile), e);
        }
    }

    private int findCabOffset() throws CabException {
        try {
            int i = 0;
            int successBytes = 0;
            while (true) {
                byte[] nextRead = new byte[1];
                int numRead = archiveStream.read(nextRead);

                if(numRead == -1) {
                    throw new CabException("This file does not contain any cabinet archive");
                }

                if((nextRead[0] & 0xFF) == 0x4D && successBytes == 0) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x53 && successBytes == 1) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x43 && successBytes == 2) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x46 && successBytes == 3) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x00 && successBytes == 4) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x00 && successBytes == 5) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x00 && successBytes == 6) {
                    successBytes++;
                } else if((nextRead[0] & 0xFF) == 0x00 && successBytes == 7) {
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

    private CFHeader getHeader() throws CabException {
        CFHeader cfHeader = new CFHeader(readBytes);
        cfHeader.populate(archiveStream);
        readBytes += (long) cfHeader.getStructureSize();

        return cfHeader;
    }

    public CFFolder getFolder() throws CabException {
        CFFolder cfFolder = new CFFolder(readBytes);
        cfFolder.populate(archiveStream);
        readBytes += (long) cfFolder.getStructureSize();
        return cfFolder;
    }

    public CFFile getFile() throws CabException {
        CFFile cfFile = new CFFile(readBytes);
        cfFile.populate(archiveStream);
        readBytes += (long) cfFile.getStructureSize();
        return cfFile;
    }

    public CFData getData(CompressionType compressionType) throws CabException {
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

    public static void main(String[] args) throws CabException, IOException {
        File tahoma32 = new File("/Users/tinou/Downloads/arial32.exe");
        Cabfile cabfile = new Cabfile(tahoma32);
        int offset = cabfile.findCabOffset();
        //System.out.println(offset);
        CFHeader header = cabfile.getHeader();

        Collection<CFFolder> cfFolders = new ArrayList<>();
        for(int i = 0; i < header.getNumberOfFolders(); i++) {
            CFFolder cfFolder = cabfile.getFolder();
            cfFolders.add(cfFolder);
            //System.out.println(cfFolder);
        }


        cabfile.jumpTo(header.coffFiles[0]);

        Collection<CFFile> cfiles = new ArrayList<>();
        for(int i = 0; i < header.getNumberOfFiles(); i++) {
            CFFile cfile = cabfile.getFile();
            cfiles.add(cfile);
            //System.out.println(cfile);
        }

        CFData cfData = cabfile.getData(CompressionType.NONE);
        //System.out.println(cfData);
        //System.out.println(Hex.encodeHexString(cfData.ab));

        CFData cfData2 = cabfile.getData(CompressionType.NONE);
        //System.out.println(cfData2);
        //System.out.println(Hex.encodeHexString(cfData2.ab));


        /*
        for(CFFile cfFile: cfiles) {
            if("fontinst.inf".equals(cfFile.getFilename())) {
                CFFolder cfFolder = cfFolders.get((int) cfFile.getFolderIndex());


                long dataIndex = cfFolder.getOffsetStartData() + cfFile.getOffsetStartDataInsideFolder();

                System.out.println("Index: ");
                System.out.println(dataIndex);

                cabfile.jumpTo(dataIndex);


                CFData cfData = cabfile.getData();


                cfFolder.dump();
                System.out.println(cfFile);
                System.out.println(cfData);

                File test = new File("/tmp/test");
                FileOutputStream stream = new FileOutputStream(test);

                stream.write(cfData.ab);
                System.out.println(Arrays.toString(cfData.ab));
            }
        } */
    }




}
