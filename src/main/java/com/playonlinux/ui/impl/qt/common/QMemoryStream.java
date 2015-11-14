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

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.trolltech.qt.core.QIODevice;

/**
 * An implementation of QIODevice for Java InMemoryStreams.
 */
public class QMemoryStream extends QIODevice {
    private static final Logger LOGGER = Logger.getLogger(QMemoryStream.class);
    private final InputStream innerStream;

    public QMemoryStream(InputStream innerStream) {
        this.innerStream = innerStream;
    }

    @Override
    public int readData(byte[] bytes) {
        try {
            return innerStream.read(bytes);
        } catch (IOException e) {
            // FIXME
            LOGGER.debug(e);
            return -1;
        }
    }

    @Override
    public int writeData(byte[] bytes) {
        return -1; // we don't need write-support at the moment.
    }
}
