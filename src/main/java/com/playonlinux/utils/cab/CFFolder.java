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

package com.playonlinux.utils.cab;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class CFFolder extends AbstractCabStructure {
    byte[] coffCabStart = new byte[4];
    byte[] cCFData = new byte[2];
    byte[] typeCompress = new byte[2];

    byte[] abReserve = new byte[256];

    CFFolder(long offset) {
        super(offset);
    }


    @Override
    public void populate(InputStream inputStream) throws CabException {
        try {
            structureSize += inputStream.read(coffCabStart);
            structureSize += inputStream.read(cCFData);
            structureSize += inputStream.read(typeCompress);

            structureSize += readVariableField(inputStream, abReserve);
        } catch (IOException e) {
            throw new CabException("Unable to extract CFFolder", e);
        }

    }

    public String toString() {
        return String.format(
                "Offset: %s\n" +
                "coffCabStart: %s\n" +
                "cCFData: %s\n" +
                "typeCompress: %s\n" +
                "abReserve: %s\n",

                offset,
                Arrays.toString(coffCabStart),
                Arrays.toString(cCFData),
                Arrays.toString(typeCompress),
                Arrays.toString(abReserve)

        );
    }

}