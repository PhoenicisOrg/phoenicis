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

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

/**
 * This component redirects {@link Process} descriptors into Java {@link OutputStream}
 * and {@link InputStream}
 *
 * This component is run in a separate thread.
 * The thread automatically dies when the process exits
 */
@Scan
public class ProcessPipe implements Service {
    private static final Logger LOGGER = Logger.getLogger(ProcessPipe.class);

    @Inject
    static ServiceManager serviceManager;

    @Inject
    static ExecutorService executorService;

    private final Process process;
    private final OutputStream redirectOutputStream;
    private final OutputStream redirectErrorStream;
    private final InputStream redirectInputStream;
    private boolean running = true;

    /**
     * Creates an instance
     * @param process The given process
     * @param outputStream the OutputStream where stdout will be redirected to
     * @param errorStream the OutputStream where stderr will be redirected to
     * @param inputStream the InputStream where stdin will take data from
     */
    public ProcessPipe(Process process,
                       OutputStream outputStream,
                       OutputStream errorStream,
                       InputStream inputStream) {
        this.process = process;
        this.redirectOutputStream = outputStream;
        this.redirectErrorStream = errorStream;
        this.redirectInputStream = inputStream;
    }

    @Override
    public void shutdown() {
        this.running = false;
        try {
            this.redirectOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("Error occured while trying to close streams", e);
        }

        try {
            this.redirectInputStream.close();
        } catch (IOException e) {
            LOGGER.error("Error occured while trying to close streams", e);
        }

        try {
            this.redirectErrorStream.close();
        } catch (IOException e) {
            LOGGER.error("Error occured while trying to close streams", e);
        }
    }

    @Override
    public void init() {
        executorService.submit(() -> {
            final InputStream inputStream = process.getInputStream();
            final InputStream errorStream = process.getErrorStream();
            final OutputStream outputStream = process.getOutputStream();


            byte[] blocksStderr = new byte[128];
            byte[] blocksStdout = new byte[128];
            byte[] blocksStdin = new byte[128];

            while (running) {
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
                        running = false;
                        break;
                    }


                } catch (IOException e) {
                    running = false;
                }
            }

            this.stop();
        });
    }

    private void stop() {
        serviceManager.unregister(this);
    }
}
