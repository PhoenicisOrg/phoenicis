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

import com.playonlinux.win32.Word;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * PE Executable ImageDOSHeader
 */
public class ImageDOSHeader {
    public static final int IMAGE_DOS_HEADER_SIZE = 64;

    final Word eMagic;                     // Magic number
    final Word eCblp;                      // Bytes on last page of file
    final Word eCp;                        // Pages in file
    final Word eCrlc;                      // Relocations
    final Word eCparhdr;                   // Size of header in paragraphs
    final Word eMinalloc;                  // Minimum extra paragraphs needed
    final Word eMaxalloc;                  // Maximum extra paragraphs needed
    final Word eSs;                        // Initial (relative) SS value
    final Word eSp;                        // Initial SP value
    final Word eCsum;                      // Checksum
    final Word eIp;                        // Initial IP value
    final Word eCs;                        // Initial (relative) CS value
    final Word eLfarlc;                    // File address of relocation table
    final Word eOvno;                      // Overlay number
    final Word[] eRes = new Word[4];       // Reserved Words
    final Word eOemid;                     // OEM identifier (for eOeminfo)
    final Word eOeminfo;                   // OEM information; eOemid specific
    final Word[] eRes2 = new Word[10];     // Reserved Words
    final Integer eLfanew;                 // File address of new exe header

    ImageDOSHeader(byte[] bytes) {
        if(bytes.length != IMAGE_DOS_HEADER_SIZE) {
            throw new IllegalStateException("An ImageDOSHeader should be "+IMAGE_DOS_HEADER_SIZE+" bytes long");
        }

        eMagic = new Word(bytes, 0);
        eCblp = new Word(bytes, 2);
        eCp = new Word(bytes, 4);
        eCrlc = new Word(bytes, 6);
        eCparhdr = new Word(bytes, 8);
        eMinalloc = new Word(bytes, 10);
        eMaxalloc = new Word(bytes, 12);
        eSs = new Word(bytes, 14);
        eSp = new Word(bytes, 16);
        eCsum = new Word(bytes, 18);
        eIp = new Word(bytes, 20);
        eCs = new Word(bytes, 22);
        eLfarlc = new Word(bytes, 24);

        for(int i = 0; i < 4; i++) {
            eRes[i] = new Word(bytes, 26 + 2*i);
        }

        eOemid = new Word(bytes, 34);
        eOeminfo = new Word(bytes, 36);

        for(int i = 0; i < 10; i++) {
            eRes2[i] = new Word(bytes, 38 + 2*i);
        }

        eOvno = new Word(bytes, 58);
        eLfanew = ByteBuffer.wrap(bytes, 60, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
