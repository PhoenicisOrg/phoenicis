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

public class ImageFileHeader {
    public static final int IMAGE_FILE_HEADER_SIZE = 20;

    final Word machine;
    final Word numberOfSections;
    final DWord timeDateStamp;
    final DWord pointerToSymbolTable;
    final DWord numberOfSymbols;
    final Word sizeOfOptionalHeader;
    final Word characteristics;

    public ImageFileHeader(byte[] bytes, int offset) {
        if(bytes.length != IMAGE_FILE_HEADER_SIZE + offset) {
            throw new IllegalStateException("An ImageFileHeader should be "+IMAGE_FILE_HEADER_SIZE+" bytes long. Got "+bytes.length);
        }

        machine = new Word(bytes, offset);
        numberOfSections = new Word(bytes, 2 + offset);
        timeDateStamp = new DWord(bytes, 4 + offset);
        pointerToSymbolTable = new DWord(bytes, 8 + offset);
        numberOfSymbols = new DWord(bytes, 12 + offset);
        sizeOfOptionalHeader = new Word(bytes, 16 + offset);
        characteristics = new Word(bytes, 18 + offset);
    }
}
