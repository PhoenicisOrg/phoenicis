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

package com.playonlinux.win32;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Represents a WIN32 WORD
 * A 16-bit unsigned integer. The range is 0 through 65535 decimal.
 */
public class UShort {
    private final short word;

    public UShort(short word) {
        this.word = word;
    }

    public UShort(byte... bytes) {
        word = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public UShort(byte[] bytes, int offset) {
        word = ByteBuffer.wrap(bytes, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public short get() {
        return word;
    }

    public int getUnsignedValue() {
        return word & 0xffff;
    }
}
