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

package com.playonlinux.win32.pe;

import com.playonlinux.win32.pe.rsrc.RsrcSection;
import org.apache.commons.io.input.CountingInputStream;

import java.io.IOException;
import java.io.InputStream;

public final class PEReader {
    PEReader() {
        // Utility class
    }

    public static PEFile parseExecutable(InputStream inputStream) throws IOException {
        try(CountingInputStream executableInputStream = new CountingInputStream(inputStream)) {
            final ImageDOSHeader imageDOSHeader = readDosHeader(executableInputStream);
            final byte[] realModeStubProgram = readRealModeStubProgram(executableInputStream, imageDOSHeader);
            final ImageNTHeaders imageNTHeaders = readImageNTHeaders(executableInputStream);
            final SectionHeader[] sectionHeaders = readSectionHeaders(executableInputStream, imageNTHeaders);
            final RsrcSection resourceSection = readResourceSection(executableInputStream, sectionHeaders);
            return new PEFile(imageDOSHeader, realModeStubProgram, imageNTHeaders, sectionHeaders, resourceSection);
        }
    }

    private static RsrcSection readResourceSection(CountingInputStream executableInputStream, SectionHeader[] sectionHeaders) throws IOException {
        SectionHeader rsrcSectionHeader = null;
        for (SectionHeader sectionHeader : sectionHeaders) {
            if (".rsrc\u0000\u0000\u0000".equals(new String(sectionHeader.name))) {
                rsrcSectionHeader = sectionHeader;
            }
        }

        if(rsrcSectionHeader == null) {
            return null;
        }

        long numberToSkip = rsrcSectionHeader.pointerToRawData.getUnsignedValue() - executableInputStream.getCount();
        executableInputStream.skip(numberToSkip);
        byte[] rsrcSection = new byte[(int) rsrcSectionHeader.sizeOfRawData.getUnsignedValue()];
        executableInputStream.read(rsrcSection);

        return new RsrcSection(rsrcSection);
    }

    private static SectionHeader[] readSectionHeaders(CountingInputStream executableInputStream, ImageNTHeaders imageNTHeaders) throws IOException {
        final int numberOfSectionHeaders = imageNTHeaders.fileHeader.numberOfSections.getUnsignedValue();
        final SectionHeader[] sectionHeaders = new SectionHeader[numberOfSectionHeaders];
        for(int i = 0; i < numberOfSectionHeaders; i++) {
            byte[] sectionHeaderBytes = new byte[SectionHeader.SECTION_HEADER_SIZE];
            executableInputStream.read(sectionHeaderBytes);
            sectionHeaders[i] = new SectionHeader(sectionHeaderBytes);
        }
        return sectionHeaders;
    }

    private static ImageNTHeaders readImageNTHeaders(InputStream executableInputStream) throws IOException {
        final byte[] byteImageNTHeader = new byte[ImageNTHeaders.IMAGE_NT_HEADER_SIZE];
        executableInputStream.read(byteImageNTHeader);
        ImageNTHeaders imageNTHeaders = new ImageNTHeaders(byteImageNTHeader);
        int optionalHeaderSize = imageNTHeaders.fileHeader.sizeOfOptionalHeader.getUnsignedValue();
        final byte[] optionalHeader = new byte[optionalHeaderSize];
        executableInputStream.read(optionalHeader);
        imageNTHeaders.readOptionalHeader(optionalHeader);
        return imageNTHeaders;
    }

    private static byte[] readRealModeStubProgram(InputStream executableInputStream, ImageDOSHeader imageDOSHeader) throws IOException {
        final byte[] realModeStubProgram = new byte[imageDOSHeader.e_lfanew - ImageDOSHeader.IMAGE_DOS_HEADER_SIZE];
        executableInputStream.read(realModeStubProgram);
        return realModeStubProgram;
    }

    private static ImageDOSHeader readDosHeader(InputStream executableInputStream) throws IOException {
        /* DOS Header */
        final byte[] byteImageDosHeader = new byte[ImageDOSHeader.IMAGE_DOS_HEADER_SIZE];
        executableInputStream.read(byteImageDosHeader);
        return new ImageDOSHeader(byteImageDosHeader);
    }
}
