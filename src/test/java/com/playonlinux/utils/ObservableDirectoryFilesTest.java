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

import com.google.common.io.Files;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.utils.observer.ObservableDirectoryFiles;
import com.playonlinux.utils.observer.Observer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class ObservableDirectoryFilesTest {

    private static final int CHECK_INTERVAL = 100;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testObservableDirectory_DirectoryDoesNotExist_ExceptionThrown() throws PlayOnLinuxException {
        expectedEx.expect(PlayOnLinuxException.class);
        expectedEx.expectMessage(String.format("The directory %s does not exist", "/tmp/unexistingDirectory"));

        new ObservableDirectoryFiles(new File("/tmp/unexistingDirectory"));
    }

    @Test
    public void testObservableDirectory_DirectoryIsInFactAFile_ExceptionThrown() throws PlayOnLinuxException, IOException {
        File temporaryFile = File.createTempFile("observableDirectoryTest", "txt");

        expectedEx.expect(PlayOnLinuxException.class);
        expectedEx.expectMessage(String.format("The file %s is not a valid directory", temporaryFile.getAbsolutePath()));

        new ObservableDirectoryFiles(temporaryFile);
    }

    @Test
    public void testObservableDirectory_dontChangeAnything_ObservableIsNotNotified() throws PlayOnLinuxException,
            InterruptedException {
        File temporaryDirectory = Files.createTempDir();


        ObservableDirectoryFiles observableDirectoryFiles = new ObservableDirectoryFiles(temporaryDirectory);
        observableDirectoryFiles.setCheckInterval(CHECK_INTERVAL);

        Observer observer = mock(Observer.class);

        observableDirectoryFiles.addObserver(observer);

        observableDirectoryFiles.start();

        Thread.sleep(2 * CHECK_INTERVAL);

        observableDirectoryFiles.stop();

        temporaryDirectory.delete();

        // Notified once for the creation
        verify(observer, times(1)).update(any(ObservableDirectoryFiles.class), anyObject());
    }

    @Test
    public void testObservableDirectory_createANewFile_ObservableIsNotified() throws PlayOnLinuxException,
            InterruptedException, IOException {
        File temporaryDirectory = Files.createTempDir();

        ObservableDirectoryFiles observableDirectoryFiles = new ObservableDirectoryFiles(temporaryDirectory);
        observableDirectoryFiles.setCheckInterval(CHECK_INTERVAL);

        Observer observer = mock(Observer.class);
        observableDirectoryFiles.addObserver(observer);
        observableDirectoryFiles.start();
        File createdFile = new File(temporaryDirectory, "file.txt");
        Thread.sleep(2 * CHECK_INTERVAL);
        createdFile.createNewFile();
        Thread.sleep(10 * CHECK_INTERVAL);

        observableDirectoryFiles.stop();

        temporaryDirectory.delete();

        verify(observer, times(2)).update(any(ObservableDirectoryFiles.class), anyObject());
    }

}