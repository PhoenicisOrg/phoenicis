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

abstract class AbstractCabStructure {
    protected final long offset;
    protected int structureSize;

    AbstractCabStructure(long offset) {
        this.offset = offset;
    }


    abstract public void populate(InputStream inputStream) throws CabException;

    protected int readVariableField(InputStream inputStream, byte[] results) throws CabException {
        int i = 0;
        while(true) {
            byte nextByte[] = new byte[1];
            int nbReadbytes;
            try {
                nbReadbytes = inputStream.read(nextByte);
            } catch (IOException e) {
                throw new CabException("Unable to read input stream", e);
            }
            if(nbReadbytes == -1) {
                throw new CabException("This archive seems to be corrupted");
            }
            assert 1 == nbReadbytes;
            results[i] = nextByte[0];
            i++;
            if(nextByte[0] == '\0') {
                break;
            }
        }
        return i;
    }

    protected long decodeLittleEndian(byte[] bytes) {
        if(bytes.length == 4) {
            return ((bytes[3] & 0xFF) << 24) | ((bytes[2] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8)  | (bytes[0] & 0xFF);
        } else {
            return ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
        }

    }

    public int getStructureSize() {
        return structureSize;
    }
}
