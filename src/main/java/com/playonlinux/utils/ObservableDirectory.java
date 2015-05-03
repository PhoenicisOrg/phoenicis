package com.playonlinux.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Observable;

public class ObservableDirectory extends Observable {
    private int checkInterval = 1000;
    private final File directoryToObserve;
    private final ObservableDirectoryThread observableDirectoryThread;

    public ObservableDirectory(File directoryToObserve) throws PlayOnLinuxError {
        this.directoryToObserve = directoryToObserve;
        if(!directoryToObserve.exists()) {
            throw new PlayOnLinuxError(String.format("The directory %s does not exist",
                    directoryToObserve.toString()));
        }
        if(!directoryToObserve.isDirectory()) {
            throw new PlayOnLinuxError(String.format("The file %s is not a valid directory",
                    directoryToObserve.toString()));
        }

        observableDirectoryThread = new ObservableDirectoryThread(this);
    }

    void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
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
