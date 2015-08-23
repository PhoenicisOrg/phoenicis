/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.ui.impl.qt.common;

import com.trolltech.qt.core.QIODevice;

import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of QIODevice for Java InMemoryStreams.
 */
public class QMemoryStream extends QIODevice {

    private InputStream innerStream;

    public QMemoryStream(InputStream innerStream){
        this.innerStream = innerStream;
    }

    @Override
    public int readData(byte[] bytes) {
        try{
            return innerStream.read(bytes);
        }catch(IOException e){
            //FIXME
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int writeData(byte[] bytes) {
        return -1; //we don't need write-support at the moment.
    }
}
