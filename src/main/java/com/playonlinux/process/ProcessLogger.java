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

package com.playonlinux.process;

import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.log.LogStream;
import com.playonlinux.services.BackgroundService;
import com.playonlinux.services.BackgroundServiceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Scan
public class ProcessLogger implements BackgroundService {
    @Inject
    private static BackgroundServiceManager backgroundServiceManager;

    private final Process process;
    private final LogStream logContext;
    private boolean running = true;

    public ProcessLogger(Process process, LogStream logContext) {
        this.process = process;
        this.logContext = logContext;
    }

    @Override
    public void shutdown() {
        this.running = false;
    }

    @Override
    public void start() {
        final InputStream inputStream = process.getInputStream();
        final InputStream errorStream = process.getErrorStream();

        BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(errorStream));

        while(running) {
            try {
                String stdErrNextLine = stdErrReader.readLine();
                String stdOutNextLine = stdOutReader.readLine();


                if(stdErrNextLine == null && stdOutNextLine == null && !process.isAlive()) {
                    running = false;
                }

                if(stdErrNextLine != null) {
                    logContext.write(stdErrNextLine.getBytes());
                }

                if(stdOutNextLine != null) {
                    logContext.write(stdOutNextLine.getBytes());
                }

            } catch (IOException e) {
                running = false;
            }
        }

        this.stop();
    }

    private void stop() {
        backgroundServiceManager.unregister(this);
    }
}
