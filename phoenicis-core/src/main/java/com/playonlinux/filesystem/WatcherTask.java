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

package com.playonlinux.filesystem;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.log4j.Logger;

public class WatcherTask<T> implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(WatcherTask.class);

    private final File observedDirectory;
    private final Supplier<T> watchedContentToCheck;
    private Consumer<T> changeConsumer;
    private final int checkInterval;
    private T lastDirectoryContent;

    private volatile Boolean running = true;

    public WatcherTask(Supplier<T> watchedContentToCheck, File observedDirectory, int checkInterval) {
        this.observedDirectory = observedDirectory;
        this.watchedContentToCheck = watchedContentToCheck;
        this.checkInterval = checkInterval;
    }

    public synchronized void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (this.isRunning()) {
            if (observedDirectory.exists()) {
                T directoryContent = watchedContentToCheck.get();

                if (directoryContent.equals(lastDirectoryContent)) {
                    synchronized (observedDirectory) {
                        if (changeConsumer != null) {
                            changeConsumer.accept(directoryContent);
                        } else {
                            continue;
                        }
                    }
                }

                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                    LOGGER.debug(e);
                    this.stop();
                }
                lastDirectoryContent = directoryContent;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setOnChange(Consumer<T> changeConsumer) {
        synchronized (observedDirectory) {
            this.changeConsumer = changeConsumer;
            if (lastDirectoryContent != null) {
                changeConsumer.accept(lastDirectoryContent);
            }
        }
    }
}