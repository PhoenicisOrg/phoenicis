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

package com.playonlinux.utils;

import com.playonlinux.common.dto.ProgressStateDTO;
import com.playonlinux.domain.PlayOnLinuxException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import java.io.File;

public class ObservableDirectorySize extends AbstractObservableDirectory {
    private final long startSize;
    private final long endSize;
    private int checkInterval = 1000;
    private final File observedDirectory;
    private final ObservableDirectoryThread observableDirectoryThread;
    private Logger logger = Logger.getLogger(this.getClass());

    public ObservableDirectorySize(File observedDirectory, long startSize, long endSize) throws PlayOnLinuxException {
        this.startSize = startSize;
        this.endSize = endSize;

        this.observedDirectory = observedDirectory;
        if(!observedDirectory.exists()) {
            throw new PlayOnLinuxException(String.format("The directory %s does not exist",
                    observedDirectory.toString()));
        }
        if(!observedDirectory.isDirectory()) {
            throw new PlayOnLinuxException(String.format("The file %s is not a valid directory",
                    observedDirectory.toString()));
        }

        observableDirectoryThread = new ObservableDirectoryThread(this);
    }

    @Override
    public void start() {
        observableDirectoryThread.start();
    }

    @Override
    public void stop() {
        observableDirectoryThread.stopChecking();
    }

    public File getObservedDirectory() {
        return observedDirectory;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append(observedDirectory.getName())
                .append(checkInterval)
                .toString();
    }

    private class ObservableDirectoryThread extends Thread {
        private final ObservableDirectorySize observableDirectorySize;
        private volatile Boolean running;

        ObservableDirectoryThread(ObservableDirectorySize observableDirectorySize) {
            super();
            this.running = false;
            this.observableDirectorySize = observableDirectorySize;
        }

        @Override
        public synchronized void start() {
            this.running = true;
            super.start();
        }

        public synchronized void stopChecking() {
            this.running = false;
        }

        @Override
        public void run() {
            long lastDirectorySize;
            while(this.isRunning()) {
                try {
                    lastDirectorySize = FileUtils.sizeOfDirectory(observedDirectory);
                    final double percentage = 100. * (double) (lastDirectorySize - startSize) / (double) (endSize - startSize);
                    ProgressStateDTO progressStateDTO = new ProgressStateDTO();
                    progressStateDTO.setPercent(percentage);
                    progressStateDTO.setState(ProgressStateDTO.State.PROGRESSING);
                    this.observableDirectorySize.setChanged();
                    this.observableDirectorySize.notifyObservers(progressStateDTO);
                } catch(IllegalArgumentException e) {
                    logger.info(String.format("Got IllegalArgumentException while checking the directory size:s. Ignoring", observedDirectory));
                }

                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                    this.stopChecking();
                }
            }
        }

        public boolean isRunning() {
            return running;
        }
    }
}
