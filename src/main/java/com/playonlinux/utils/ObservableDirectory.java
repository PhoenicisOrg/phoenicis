package com.playonlinux.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Observable;

public class ObservableDirectory extends Observable {
    final int CHECK_INTERVAL = 2000;
    private final File directoryToObserve;
    private final ObservableDirectoryThread observableDirectoryThread;

    public ObservableDirectory(File directoryToObserve) throws PlayOnLinuxError {
        this.directoryToObserve = directoryToObserve;
        if(!directoryToObserve.isDirectory()) {
            throw new PlayOnLinuxError(String.format("The file %s is not a valid directory",
                    directoryToObserve.toString()));
        }

        observableDirectoryThread = new ObservableDirectoryThread(this);
    }

    public void start() {
        observableDirectoryThread.start();
    }

    public void stop() {
        observableDirectoryThread.stopChecking();
    }

    protected File[] findFiles() {
        return directoryToObserve.listFiles();
    }

    private class ObservableDirectoryThread extends Thread {
        private final ObservableDirectory observableDirectory;
        private Boolean running;

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

        public void run() {
            File[] lastDirectoryContent = null;
            while(this.isRunning()) {
                File[] directoryContent = observableDirectory.findFiles();
                if(!Arrays.equals(directoryContent, lastDirectoryContent)) {
                    this.observableDirectory.notifyObservers(directoryContent);
                }
                try {
                    Thread.sleep(CHECK_INTERVAL);
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
