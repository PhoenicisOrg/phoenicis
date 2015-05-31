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

public class CFData extends AbstractCabStructure {
    byte[] csum = new byte[4];
    byte[] cbData = new byte[2];
    byte[] cbUncomp = new byte[2];

    byte[] abReserve = new byte[256];
    byte[] ab = new byte[256];

    CFData(int offset) {
        super(offset);
    }

    @Override
    public void populate(InputStream inputStream) throws CabException {
        try {
            structureSize += inputStream.read(csum);
            structureSize += inputStream.read(cbData);
            structureSize += inputStream.read(cbUncomp);

            structureSize += readVariableField(inputStream, abReserve);
            structureSize += readVariableField(inputStream, ab);

        } catch (IOException e) {
            throw new CabException("Unable to extract CFFolder", e);
        }
    }

    public String toString() {
        return String.format(
                "Offset: %s\n",


                offset

        );
    }

}