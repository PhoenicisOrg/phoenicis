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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.io.Files;

public class DirectoryWatcherFilesTest {
    private static final int CHECK_INTERVAL = 100;
    private ExecutorService mockExecutorService = Executors.newSingleThreadExecutor();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testObservableDirectory_DirectoryIsInFactAFile_ExceptionThrown() throws IOException {
        File temporaryFile = File.createTempFile("observableDirectoryTest", "txt");
        temporaryFile.deleteOnExit();
        expectedEx.expect(IllegalStateException.class);
        expectedEx
                .expectMessage(String.format("The file %s is not a valid directory", temporaryFile.getAbsolutePath()));

        DirectoryWatcherFiles directoryWatcherFiles = new DirectoryWatcherFiles(mockExecutorService, temporaryFile,
                CHECK_INTERVAL);
        directoryWatcherFiles.close();
    }

    @Test
    public void testObservableDirectory_dontChangeAnything_ObservableOnlyNotifiedOnce() throws InterruptedException {
        final File temporaryDirectory = Files.createTempDir();

        try (DirectoryWatcherFiles directoryWatcherFiles = new DirectoryWatcherFiles(mockExecutorService,
                temporaryDirectory, CHECK_INTERVAL)) {
            final Consumer<List<File>> mockConsumer = mock(Consumer.class);
            directoryWatcherFiles.setOnChange(mockConsumer);
            Thread.sleep(2 * CHECK_INTERVAL);

            temporaryDirectory.delete();

            verify(mockConsumer, times(1)).accept(any(List.class));
        }
    }

    @Test
    public void testObservableDirectory_createANewFile_ObservableIsNotifiedTwice()
            throws InterruptedException, IOException {
        final File temporaryDirectory = Files.createTempDir();

        try (DirectoryWatcherFiles directoryWatcherFiles = new DirectoryWatcherFiles(mockExecutorService,
                temporaryDirectory, CHECK_INTERVAL)) {
            final Consumer<List<File>> mockConsumer = mock(Consumer.class);
            directoryWatcherFiles.setOnChange(mockConsumer);

            File createdFile = new File(temporaryDirectory, "file.txt");
            Thread.sleep(2 * CHECK_INTERVAL);
            createdFile.createNewFile();
            Thread.sleep(10 * CHECK_INTERVAL);

            temporaryDirectory.delete();

            verify(mockConsumer, times(2)).accept(any(List.class));
        }
    }
}