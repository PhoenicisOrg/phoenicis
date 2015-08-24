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

    final Word e_magic;                     // Magic number
    final Word e_cblp;                      // Bytes on last page of file
    final Word e_cp;                        // Pages in file
    final Word e_crlc;                      // Relocations
    final Word e_cparhdr;                   // Size of header in paragraphs
    final Word e_minalloc;                  // Minimum extra paragraphs needed
    final Word e_maxalloc;                  // Maximum extra paragraphs needed
    final Word e_ss;                        // Initial (relative) SS value
    final Word e_sp;                        // Initial SP value
    final Word e_csum;                      // Checksum
    final Word e_ip;                        // Initial IP value
    final Word e_cs;                        // Initial (relative) CS value
    final Word e_lfarlc;                    // File address of relocation table
    final Word e_ovno;                      // Overlay number
    final Word[] e_res = new Word[4];       // Reserved Words
    final Word e_oemid;                     // OEM identifier (for e_oeminfo)
    final Word e_oeminfo;                   // OEM information; e_oemid specific
    final Word[] e_res2 = new Word[10];     // Reserved Words
    final Integer e_lfanew;                 // File address of new exe header

    ImageDOSHeader(byte[] bytes) {
        if(bytes.length != IMAGE_DOS_HEADER_SIZE) {
            throw new IllegalStateException("An ImageDOSHeader should be "+IMAGE_DOS_HEADER_SIZE+" bytes long");
        }

        e_magic = new Word(bytes, 0);
        e_cblp = new Word(bytes, 2);
        e_cp = new Word(bytes, 4);
        e_crlc = new Word(bytes, 6);
        e_cparhdr = new Word(bytes, 8);
        e_minalloc = new Word(bytes, 10);
        e_maxalloc = new Word(bytes, 12);
        e_ss = new Word(bytes, 14);
        e_sp = new Word(bytes, 16);
        e_csum = new Word(bytes, 18);
        e_ip = new Word(bytes, 20);
        e_cs = new Word(bytes, 22);
        e_lfarlc = new Word(bytes, 24);

        for(int i = 0; i < 4; i++) {
            e_res[i] = new Word(bytes, 26 + 2*i);
        }

        e_oemid = new Word(bytes, 34);
        e_oeminfo = new Word(bytes, 36);

        for(int i = 0; i < 10; i++) {
            e_res2[i] = new Word(bytes, 38 + 2*i);
        }

        e_ovno = new Word(bytes, 58);
        e_lfanew = ByteBuffer.wrap(bytes, 60, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
