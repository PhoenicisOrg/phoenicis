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

package org.phoenicis.win32;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Represents a WIN32 ULONG A 32-bit unsigned integer. The range is 0 through
 * 4294967295 decimal.
 */
public class ULong {
    private final int ulongContent;

    public ULong(int ulongContent) {
        this.ulongContent = ulongContent;
    }

    public ULong(byte[] bytes, int offset) {
        ulongContent = ByteBuffer.wrap(bytes, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public int get() {
        return ulongContent;
    }

    public long getUnsignedValue() {
        return ulongContent & 0xFFFFFFFFL;
    }
}
