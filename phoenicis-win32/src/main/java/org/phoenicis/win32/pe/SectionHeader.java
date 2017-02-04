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

package org.phoenicis.win32.pe;

import org.phoenicis.win32.ULong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SectionHeader {
    public static final int SECTION_HEADER_SIZE = 40;

    final byte[] name = new byte[8];
    final ULong physicalAddressOrVirtualSize;
    final ULong virtualAddress;
    final ULong sizeOfRawData;
    final ULong pointerToRawData;
    final ULong pointerToRelocations;
    final ULong pointerToLinenumbers;
    final Short numberOfRelocations;
    final Short numberOfLineNumbers;
    final ULong characteristics;

    public SectionHeader(byte[] bytes) {
        if(bytes.length != SECTION_HEADER_SIZE) {
            throw new IllegalStateException("Section Header should be "+SECTION_HEADER_SIZE+" bytes");
        }
        System.arraycopy(bytes, 0, name, 0, 8);
        physicalAddressOrVirtualSize = new ULong(bytes, 8);
        virtualAddress = new ULong(bytes, 12);
        sizeOfRawData = new ULong(bytes, 16);
        pointerToRawData = new ULong(bytes, 20);
        pointerToRelocations = new ULong(bytes, 24);
        pointerToLinenumbers = new ULong(bytes, 28);
        numberOfRelocations = readShort(bytes, 32);
        numberOfLineNumbers = readShort(bytes, 34);
        characteristics = new ULong(bytes, 36);
    }

    private Short readShort(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
}
