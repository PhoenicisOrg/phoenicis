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

package com.playonlinux.win32.pe;

import com.playonlinux.win32.DWord;
import com.playonlinux.win32.Word;

public class ImageOptionalHeader {
    public static final int IMAGE_OPTIONAL_HEADER_BASE_SIZE = 96;

    final Word magic;
    final Byte majorLinkerVersion;
    final Byte minorLinkerVersion;
    final DWord sizeOfCode;
    final DWord sizeOfInitializedData;
    final DWord sizeOfUninitializedData;
    final DWord addressOfEntryPoint;
    final DWord baseOfCode;
    final DWord baseOfData;
    final DWord imageBase;
    final DWord sectionAlignment;
    final DWord fileAlignment;
    final Word majorOperatingSystemVersion;
    final Word minorOperatingSystemVersion;
    final Word majorImageVersion;
    final Word minorImageVersion;
    final Word majorSubsystemVersion;
    final Word minorSubsystemVersion;
    final DWord win32VersionValue;
    final DWord sizeOfImage;
    final DWord sizeOfHeaders;
    final DWord checkSum;
    final Word subsystem;
    final Word dllCharacteristics;
    final DWord sizeOfStackReserve;
    final DWord sizeOfStackCommit;
    final DWord sizeOfHeapReserve;
    final DWord sizeOfHeapCommit;
    final DWord loaderFlags;
    final DWord numberOfRvaAndSizes;
    final ImageDataDirectory[] dataDirectory;

    public ImageOptionalHeader(byte[] bytes) {
        magic = new Word(bytes, 0);
        majorLinkerVersion = bytes[2];
        minorLinkerVersion = bytes[3];
        sizeOfCode = new DWord(bytes, 4);
        sizeOfInitializedData = new DWord(bytes, 8);
        sizeOfUninitializedData = new DWord(bytes, 12);
        addressOfEntryPoint = new DWord(bytes, 16);
        baseOfCode = new DWord(bytes, 20);
        baseOfData = new DWord(bytes, 24);
        imageBase = new DWord(bytes, 28);
        sectionAlignment = new DWord(32);
        fileAlignment = new DWord(36);
        majorOperatingSystemVersion = new Word(bytes, 40);
        minorOperatingSystemVersion = new Word(bytes, 42);
        majorImageVersion = new Word(bytes, 44);
        minorImageVersion = new Word(bytes, 46);
        majorSubsystemVersion = new Word(bytes, 48);
        minorSubsystemVersion = new Word(bytes, 50);
        win32VersionValue = new DWord(bytes, 52);
        sizeOfImage = new DWord(bytes, 56);
        sizeOfHeaders = new DWord(bytes, 60);
        checkSum = new DWord(bytes, 64);
        subsystem = new Word(bytes, 68);
        dllCharacteristics = new Word(bytes, 70);
        sizeOfStackReserve = new DWord(bytes, 72);
        sizeOfStackCommit = new DWord(bytes, 76);
        sizeOfHeapReserve = new DWord(bytes, 80);
        sizeOfHeapCommit = new DWord(bytes, 84);
        loaderFlags = new DWord(bytes, 88);
        numberOfRvaAndSizes = new DWord(bytes, 92);

        int numberOfDirectoryEntries = (bytes.length - IMAGE_OPTIONAL_HEADER_BASE_SIZE) / 8;
        dataDirectory = new ImageDataDirectory[numberOfDirectoryEntries];

        for(int i = 0; i < numberOfDirectoryEntries; i++) {
            dataDirectory[i] = new ImageDataDirectory(bytes, IMAGE_OPTIONAL_HEADER_BASE_SIZE + i * 8);
        }
    }

}
