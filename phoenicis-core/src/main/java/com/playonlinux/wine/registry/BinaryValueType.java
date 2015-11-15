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

package com.playonlinux.wine.registry;

public class BinaryValueType extends AbstractValueType {
    private final byte[] binaries;

    public BinaryValueType(byte[] binaries) {
        this.binaries = binaries.clone();
    }

    @Override
    String getText() {
        final String[] binariesAsString = new String[binaries.length];
        int i = 0;
        for(byte readByte: binaries) {
            binariesAsString[i] = Integer.toString((int)readByte + 128);
            i++;
        }
        return String.join(",", binariesAsString);
    }

    @Override
    public String toRegString() {
        return String.format("hex:%s", this.getText());
    }
}
