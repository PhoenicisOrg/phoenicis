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

import com.playonlinux.app.PlayOnLinuxException;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ObservableDefaultDirectoryFiles extends ObservableDefaultDirectory<File[]> {
    private final ObservableDirectoryThread observableDirectoryThread;

    public ObservableDefaultDirectoryFiles(File observedDirectory) throws PlayOnLinuxException {
        this.observedDirectory = observedDirectory;
        validate();
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


    /* TODO: Test this method with better coverage */
    protected File[] findFiles() {
        File[] files = observedDirectory.listFiles();
        List<File> filesFiltered = new LinkedList<>();
        assert files != null;
        for(File file: files) {
            if (!file.getName().startsWith(".")) {
                filesFiltered.add(file);
            }
        }

        return filesFiltered.toArray(new File[filesFiltered.size()]);
    }


    private class ObservableDirectoryThread extends Thread {
        private final ObservableDefaultDirectoryFiles observableDirectoryFiles;
        private volatile Boolean running;

        ObservableDirectoryThread(ObservableDefaultDirectoryFiles observableDirectoryFiles) {
            super();
            this.running = false;
            this.observableDirectoryFiles = observableDirectoryFiles;
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
                if(observedDirectory.exists()) {
                    File[] directoryContent = observableDirectoryFiles.findFiles();
                    if (!Arrays.equals(directoryContent, lastDirectoryContent)) {
                        this.observableDirectoryFiles.notifyObservers(directoryContent);
                    }
                    try {
                        Thread.sleep(checkInterval);
                    } catch (InterruptedException e) {
                        this.stopChecking();
                    }
                    lastDirectoryContent = directoryContent;
                }
            }
        }

        public boolean isRunning() {
            return running;
        }
    }
}
