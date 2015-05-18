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

import com.playonlinux.domain.PlayOnLinuxError;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class ObservableDirectory extends Observable implements BackgroundService {
    private int checkInterval = 1000;
    private final File observedDirectory;
    private final ObservableDirectoryThread observableDirectoryThread;

    public ObservableDirectory(File observedDirectory) throws PlayOnLinuxError {
        this.observedDirectory = observedDirectory;
        if(!observedDirectory.exists()) {
            throw new PlayOnLinuxError(String.format("The directory %s does not exist",
                    observedDirectory.toString()));
        }
        if(!observedDirectory.isDirectory()) {
            throw new PlayOnLinuxError(String.format("The file %s is not a valid directory",
                    observedDirectory.toString()));
        }

        observableDirectoryThread = new ObservableDirectoryThread(this);
    }

    void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    @Override
    public void start() {
        observableDirectoryThread.start();
    }

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
    /* Protected because the user of the class should use the observer pattern and should not access directly
    to the files */

    /* TODO: Test this method with better coverage */
    protected File[] findFiles() {
        File[] files = observedDirectory.listFiles();
        List<File> filesFiltered = new LinkedList<>();
        for(File file: files) {
            if (!file.getName().startsWith(".")) {
                filesFiltered.add(file);
            }
        }

        return filesFiltered.toArray(new File[filesFiltered.size()]);
    }

    @Override
    public void shutdown() {
        this.stop();
    }


    private class ObservableDirectoryThread extends Thread {
        private final ObservableDirectory observableDirectory;
        private volatile Boolean running;

        ObservableDirectoryThread(ObservableDirectory observableDirectory) {
            super();
            this.running = false;
            this.observableDirectory = observableDirectory;
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
            File[] lastDirectoryContent = null;
            while(this.isRunning()) {
                File[] directoryContent = observableDirectory.findFiles();
                if(!Arrays.equals(directoryContent, lastDirectoryContent)) {
                    this.observableDirectory.setChanged();
                    this.observableDirectory.notifyObservers(directoryContent);
                }
                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                    this.stopChecking();
                }
                lastDirectoryContent = directoryContent;
            }
        }

        public boolean isRunning() {
            return running;
        }
    }
}
