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

package com.playonlinux.core.observer;

import com.playonlinux.core.entities.ProgressStateEntity;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import java.io.File;

public class ObservableDirectorySize extends ObservableDirectory<ProgressStateEntity> {
    private final long startSize;
    private final long endSize;
    private final ObservableDirectoryThread observableDirectoryThread;
    private static final Logger LOGGER = Logger.getLogger(ObservableDirectorySize.class);

    public ObservableDirectorySize(File observedDirectory, long startSize, long endSize) {
        this.startSize = startSize;
        this.endSize = endSize;

        this.observedDirectory = observedDirectory;
        validate();

        observableDirectoryThread = new ObservableDirectoryThread(this);
    }

    @Override
    public void shutdown() {
        observableDirectoryThread.stopChecking();
        super.shutdown();
    }

    @Override
    public void init() throws ServiceInitializationException {
        this.observableDirectoryThread.start();
    }

    @Override
    public File getObservedDirectory() {
        return observedDirectory;
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
            while(this.isRunning()) {
                try {
                    if(observedDirectory.exists()) {
                        long lastDirectorySize = FileUtils.sizeOfDirectory(observedDirectory);
                        final double percentage = 100. * (double) (lastDirectorySize - startSize) / (double) (endSize - startSize);
                        ProgressStateEntity progressStateEntity = new ProgressStateEntity.Builder()
                                .withState(ProgressStateEntity.State.PROGRESSING)
                                .withPercent(percentage)
                                .build();

                        this.observableDirectorySize.notifyObservers(progressStateEntity);
                    }
                } catch(IllegalArgumentException e) {
                    LOGGER.info(String.format("Got IllegalArgumentException while checking the directory size: %s. Ignoring", observedDirectory), e);
                }

                try {
                    Thread.sleep((long) checkInterval);
                } catch (InterruptedException e) {
                    LOGGER.debug(e);
                    this.stopChecking();
                }
            }
        }

        public boolean isRunning() {
            return running;
        }
    }
}
