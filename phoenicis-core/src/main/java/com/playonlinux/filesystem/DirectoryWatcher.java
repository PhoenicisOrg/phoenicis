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
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class DirectoryWatcher<T> implements AutoCloseable {
    protected final File observedDirectory;
    private static final int DEFAULT_CHECK_INTERVAL = 1000;
    private final WatcherTask<T> watcherTask;

    public DirectoryWatcher(ExecutorService executorService, File observedDirectory) {
        this(executorService, observedDirectory, DEFAULT_CHECK_INTERVAL);
    }

    public DirectoryWatcher(ExecutorService executorService, File observedDirectory, int checkInterval) {
        this.observedDirectory = observedDirectory;
        validate();
        watcherTask = new WatcherTask<>(this::defineWatchedObject, observedDirectory, checkInterval);
        executorService.submit(watcherTask);
    }

    protected void validate() {
        if (observedDirectory.exists() && !observedDirectory.isDirectory()) {
            throw new IllegalStateException(
                    String.format("The file %s is not a valid directory", observedDirectory.toString()));
        }
    }

    protected abstract T defineWatchedObject();

    public File getObservedDirectory() {
        return observedDirectory;
    }

    public void setOnChange(Consumer<T> consumer) {
        this.watcherTask.setOnChange(consumer);
    }

    @Override
    public void close() {
        watcherTask.stop();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(observedDirectory.getName()).toString();
    }

}
