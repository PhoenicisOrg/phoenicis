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

package com.playonlinux.core.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.playonlinux.MockContextConfig;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.InjectionException;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDirectoryFiles;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceManager;

@Scan
public class ObservableDirectoryFilesTest {
    @Inject
    static ServiceManager serviceManager;

    private static final int CHECK_INTERVAL = 100;
    private static final AbstractConfiguration testConfigFile = new MockContextConfig();

    @BeforeClass
    public static void setUpClass() throws InjectionException {
        testConfigFile.setStrictLoadingPolicy(false);
        testConfigFile.load();
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testObservableDirectory_DirectoryIsInFactAFile_ExceptionThrown() throws PlayOnLinuxException, IOException {
        File temporaryFile = File.createTempFile("observableDirectoryTest", "txt");
        temporaryFile.deleteOnExit();
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage(String.format("The file %s is not a valid directory", temporaryFile.getAbsolutePath()));

        new ObservableDirectoryFiles(temporaryFile);
    }

    @Test
    public void testObservableDirectory_dontChangeAnything_ObservableIsNotNotified() throws PlayOnLinuxException,
            InterruptedException {
        File temporaryDirectory = com.google.common.io.Files.createTempDir();

        reset(serviceManager);

        try(ObservableDirectoryFiles observableDirectoryFiles = new ObservableDirectoryFiles(temporaryDirectory)) {
            observableDirectoryFiles.setCheckInterval(CHECK_INTERVAL);

            Observer mockObserver = mock(Observer.class);

            observableDirectoryFiles.addObserver(mockObserver);

            observableDirectoryFiles.init();

            Thread.sleep(2 * CHECK_INTERVAL);

            temporaryDirectory.delete();

            verify(mockObserver, times(1)).update(any(ObservableDirectoryFiles.class), anyObject());
        }

        verify(serviceManager).unregister(any(Service.class));
    }

    @Test
    public void testObservableDirectory_createANewFile_ObservableIsNotified() throws PlayOnLinuxException,
            InterruptedException, IOException {
        File temporaryDirectory = com.google.common.io.Files.createTempDir();

        reset(serviceManager);

        try(ObservableDirectoryFiles observableDirectoryFiles = new ObservableDirectoryFiles(temporaryDirectory)) {
            observableDirectoryFiles.setCheckInterval(CHECK_INTERVAL);

            Observer observer = mock(Observer.class);
            observableDirectoryFiles.addObserver(observer);
            observableDirectoryFiles.init();
            File createdFile = new File(temporaryDirectory, "file.txt");
            Thread.sleep(2 * CHECK_INTERVAL);
            createdFile.createNewFile();
            Thread.sleep(10 * CHECK_INTERVAL);

            temporaryDirectory.delete();

            verify(observer, times(2)).update(any(ObservableDirectoryFiles.class), anyObject());
        }

        verify(serviceManager).unregister(any(Service.class));
    }

    @AfterClass
    public static void tearDownClass() throws InjectionException {
        testConfigFile.setStrictLoadingPolicy(false);
        testConfigFile.close();
    }
}