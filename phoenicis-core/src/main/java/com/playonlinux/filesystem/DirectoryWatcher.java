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

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import org.apache.commons.lang.builder.ToStringBuilder;

import lombok.Getter;

public abstract class DirectoryWatcher<T> implements AutoCloseable {
    @Getter
    protected final Path observedDirectory;
    private final WatchService watcher;
    private Consumer<T> changeConsumer;

    public DirectoryWatcher(ExecutorService executorService, Path observedDirectory) {
        try {
            validate(observedDirectory);
            this.observedDirectory = observedDirectory;
            this.watcher = FileSystems.getDefault().newWatchService();

            observedDirectory.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            executorService.submit(this::run);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to create watcher for %s", observedDirectory.toString()));
        }
    }

    private static void validate(Path observedDirectory) {
        if (!Files.isDirectory(observedDirectory)) {
            throw new IllegalStateException(
                    String.format("The file %s is not a valid directory", observedDirectory.toString()));
        }
    }

    protected abstract T defineWatchedObject();

    public void run() {
        try {
            WatchKey key = watcher.take();
            key.pollEvents();
            notifyConsumer();

            while (key.reset()) {
                key = watcher.take();
                key.pollEvents();
                notifyConsumer();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(String.format("Watcher Interupted for %s", observedDirectory.toString()));
        }
    }

    private void notifyConsumer() {
        if (changeConsumer != null) {
            changeConsumer.accept(defineWatchedObject());
        }
    }

    @Override
    public void close() {
        try {
            watcher.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to close watcher for %s", observedDirectory.toString()));
        }
    }

    public void setOnChange(Consumer<T> changeConsumer) {
        this.changeConsumer = changeConsumer;
        notifyConsumer();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(observedDirectory.getFileName()).toString();
    }
}
