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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SectionHeader {
    public static final int SECTION_HEADER_SIZE = 40;

    final byte[] name = new byte[8];
    final Integer physicalAddressOrVirtualSize;
    final Integer virtualAddress;
    final Integer sizeOfRawData;
    final Integer pointerToRawData;
    final Integer pointerToRelocations;
    final Integer pointerToLinenumbers;
    final Short numberOfRelocations;
    final Short numberOfLineNumbers;
    final Integer characteristics;

    public SectionHeader(byte[] bytes) {
        if(bytes.length != SECTION_HEADER_SIZE) {
            throw new IllegalStateException("Section Header should be "+SECTION_HEADER_SIZE+" bytes");
        }
        System.arraycopy(bytes, 0, name, 0, 8);
        physicalAddressOrVirtualSize = readInteger(bytes, 8);
        virtualAddress = readInteger(bytes, 12);
        sizeOfRawData = readInteger(bytes, 16);
        pointerToRawData = readInteger(bytes, 20);
        pointerToRelocations = readInteger(bytes, 24);
        pointerToLinenumbers = readInteger(bytes, 28);
        numberOfRelocations = readShort(bytes, 32);
        numberOfLineNumbers = readShort(bytes, 34);
        characteristics = readInteger(bytes, 36);
    }

    private Integer readInteger(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private Short readShort(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
}
