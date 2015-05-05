package com.playonlinux.utils;

import com.playonlinux.domain.PlayOnLinuxError;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Observable;

public class ObservableDirectory extends Observable implements BackgroundService {
    private int checkInterval = 1000;
    private final File observedDirectory;
    private final ObservableDirectoryThread observableDirectoryThread;
    private String serviceName;

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
    protected File[] findFiles() {
        return observedDirectory.listFiles();
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
