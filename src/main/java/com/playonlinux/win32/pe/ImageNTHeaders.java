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

import com.playonlinux.win32.DWord;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PE Executable ImageNTHeaders
 */
public class ImageNTHeaders {
    public static final int IMAGE_NT_HEADER_SIZE = ImageFileHeader.IMAGE_FILE_HEADER_SIZE + 4;
    final DWord signature;
    final ImageFileHeader fileHeader;
    ImageOptionalHeader optionalHeader;

    public ImageNTHeaders(byte[] bytes) {
        signature = new DWord(bytes, 0);
        fileHeader = new ImageFileHeader(bytes, 4);
    }

    public void readOptionalHeader(byte[] bytes) {
        optionalHeader = new ImageOptionalHeader(bytes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ImageNTHeaders.class)
                .append("signature", signature)
                .append("fileHeader", fileHeader)
                .append("optionalHeader", optionalHeader)
                .toString();
    }
}
