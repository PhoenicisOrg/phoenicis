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

package com.playonlinux.core.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class FileLogger extends OutputStream {
    private final FileOutputStream logOutputStream;
    private final boolean echoOnStdout;

    FileLogger(File logFile, boolean echoOnStdout) throws IOException {
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        this.logOutputStream = new FileOutputStream(logFile);
        this.echoOnStdout = echoOnStdout;
    }

    @Override
    public synchronized void flush() throws IOException {
        this.logOutputStream.flush();
        if (echoOnStdout) {
            System.out.flush();
        }
        super.flush();
    }

    @Override
    public synchronized void write(int b) throws IOException {
        logOutputStream.write(b);

        if (echoOnStdout) {
            System.out.write(b);
        }
    }

}
