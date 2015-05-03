package com.playonlinux.utils;

import com.google.common.io.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.util.Observer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class ObservableDirectoryTest {

    private static final int CHECK_INTERVAL = 100;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testObservableDirectory_DirectoryDoesNotExist_ExceptionThrown() throws PlayOnLinuxError {
        expectedEx.expect(PlayOnLinuxError.class);
        expectedEx.expectMessage(String.format("The directory %s does not exist", "/tmp/unexistingDirectory"));

        new ObservableDirectory(new File("/tmp/unexistingDirectory"));
    }

    @Test
    public void testObservableDirectory_DirectoryIsInFactAFile_ExceptionThrown() throws PlayOnLinuxError, IOException {
        File temporaryFile = File.createTempFile("observableDirectoryTest", "txt");

        expectedEx.expect(PlayOnLinuxError.class);
        expectedEx.expectMessage(String.format("The file %s is not a valid directory", temporaryFile.getAbsolutePath()));

        new ObservableDirectory(temporaryFile);
    }

    @Test
    public void testObservableDirectory_dontChangeAnything_ObservableIsNotNotified() throws PlayOnLinuxError,
            InterruptedException {
        File temporaryDirectory = Files.createTempDir();


        ObservableDirectory observableDirectory = new ObservableDirectory(temporaryDirectory);
        observableDirectory.setCheckInterval(CHECK_INTERVAL);
        observableDirectory.start();

        Observer observer = mock(Observer.class);

        observableDirectory.addObserver(observer);
        Thread.sleep(2 * CHECK_INTERVAL);

        observableDirectory.stop();

        temporaryDirectory.delete();

        verify(observer, never()).update(any(ObservableDirectory.class), anyObject());
    }

    @Test
    public void testObservableDirectory_createANewFile_ObservableIsNotified() throws PlayOnLinuxError,
            InterruptedException, IOException {
        File temporaryDirectory = Files.createTempDir();

        ObservableDirectory observableDirectory = new ObservableDirectory(temporaryDirectory);
        observableDirectory.setCheckInterval(CHECK_INTERVAL);

        Observer observer = mock(Observer.class);
        observableDirectory.addObserver(observer);
        observableDirectory.start();
        File createdFile = new File(temporaryDirectory, "file.txt");
        createdFile.createNewFile();
        Thread.sleep(2 * CHECK_INTERVAL);

        observableDirectory.stop();

        temporaryDirectory.delete();

        verify(observer, times(1)).update(any(ObservableDirectory.class), anyObject());
    }

}