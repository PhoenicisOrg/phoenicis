package com.playonlinux.framework;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class DownloaderTest {
    Downloader downloader;

    @Before
    public void setUp() {
        downloader = new Downloader();
    }

    @Test
    public void testDownloadGetFilenameFromURL() throws MalformedURLException {
        assertEquals("foo.bar", downloader.findFileNameFromURL(new URL("http://www.site.com/directory/foo.bar")));
    }
}