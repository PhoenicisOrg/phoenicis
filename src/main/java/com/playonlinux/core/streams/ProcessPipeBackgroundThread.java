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

package com.playonlinux.core.streams;


import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProcessPipeBackgroundThread implements Runnable {
    public static final int BLOCK_SIZE = 128;
    private static final Logger LOGGER = Logger.getLogger(ProcessPipeBackgroundThread.class);
    private final Process process;
    private final MutableBoolean running;
    private final InputStream redirectInputStream;
    private final OutputStream redirectOutputStream;
    private final OutputStream redirectErrorStream;
    private final ProcessPipe caller;

    public ProcessPipeBackgroundThread(ProcessPipe caller,
                                       MutableBoolean running,
                                       Process process,
                                       InputStream redirectInputStream,
                                       OutputStream redirectErrorStream,
                                       OutputStream redirectOutputStream) {
        this.caller = caller;
        this.running = running;
        this.process = process;
        this.redirectInputStream = redirectInputStream;
        this.redirectOutputStream = redirectOutputStream;
        this.redirectErrorStream = redirectErrorStream;
    }

    @Override
    public void run() {
            final InputStream inputStream = process.getInputStream();
            final InputStream errorStream = process.getErrorStream();
            final OutputStream outputStream = process.getOutputStream();

            byte[] blocksStderr = new byte[BLOCK_SIZE];
            byte[] blocksStdout = new byte[BLOCK_SIZE];
            byte[] blocksStdin = new byte[BLOCK_SIZE];

            while (running.isTrue()) {
                try {
                    boolean readStderr = errorStream.read(blocksStderr) != -1;
                    boolean readStdout = inputStream.read(blocksStdout) != -1;
                    boolean readStdin = redirectInputStream.read(blocksStdin) != -1;

                    if (process.isAlive() && readStdin) {
                        outputStream.write(blocksStdin);
                        outputStream.flush();
                    }

                    if (readStderr) {
                        redirectErrorStream.write(blocksStderr);
                        redirectErrorStream.flush();
                    }

                    if (readStdout) {
                        redirectOutputStream.write(blocksStdout);
                        redirectOutputStream.flush();
                    }

                    if (!process.isAlive() && !readStderr && !readStdout) {
                        running.setValue(false);
                        break;
                    }

                } catch (IOException e) {
                    LOGGER.debug(e);
                    running.setValue(false);
                }
            }

            this.caller.stop();
        }

}
