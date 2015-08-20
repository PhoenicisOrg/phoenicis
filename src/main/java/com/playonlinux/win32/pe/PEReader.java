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

import java.io.IOException;
import java.io.InputStream;

public final class PEReader {
    PEReader() {
        // Utility class
    }

    public static PEFile parseExecutable(InputStream executableInputStream) throws IOException {
        final byte[] byteImageDosHeader = new byte[ImageDOSHeader.IMAGE_DOS_HEADER_SIZE];
        executableInputStream.read(byteImageDosHeader);
        ImageDOSHeader imageDOSHeader = new ImageDOSHeader(byteImageDosHeader);
        final byte[] realModeStubProgram = new byte[imageDOSHeader.e_lfanew - ImageDOSHeader.IMAGE_DOS_HEADER_SIZE];
        executableInputStream.read(realModeStubProgram);

        final byte[] byteImageNTHeader = new byte[ImageNTHeaders.IMAGE_NT_HEADER_SIZE];
        executableInputStream.read(byteImageNTHeader);

        ImageNTHeaders imageNTHeaders = new ImageNTHeaders(byteImageNTHeader);

        int optionalHeaderSize = imageNTHeaders.fileHeader.sizeOfOptionalHeader.getUnsignedValue();

        final byte[] optionalHeader = new byte[optionalHeaderSize];

        executableInputStream.read(optionalHeader);

        imageNTHeaders.readOptionalHeader(optionalHeader);

        return new PEFile(imageDOSHeader, realModeStubProgram, imageNTHeaders, null);
    }
}
