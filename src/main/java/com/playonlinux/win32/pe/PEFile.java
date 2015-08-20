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

public class PEFile {
    public enum Architecture {
        AMD64, I386, IA64
    }
    final ImageDOSHeader imageDOSHeader;
    final byte[] realModeStubProgram;
    final ImageNTHeaders imageNTHeaders;
    final ImageOptionalHeader imageOptionalHeader;

    public PEFile(ImageDOSHeader imageDOSHeader, byte[] realModeStubProgram, ImageNTHeaders imageNTHeaders, ImageOptionalHeader imageOptionalHeader) {
        this.imageDOSHeader = imageDOSHeader;
        this.realModeStubProgram = realModeStubProgram;
        this.imageNTHeaders = imageNTHeaders;
        this.imageOptionalHeader = imageOptionalHeader;
    }

    public Architecture getArchitecture() {
        switch (imageNTHeaders.fileHeader.machine.getUnsignedValue()) {
            case 0x14c:
                return Architecture.I386;
            case 0x8664:
                return Architecture.AMD64;
            case 0x0200:
                return Architecture.IA64;
            default:
                throw new IllegalArgumentException("The file is not a valid .exe file");
        }

    }
}
