/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.tools.http;

import org.phoenicis.tools.files.FileSizeUtilities;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class DownloaderTest {
    private static URL mockServerURL;
    private static URL mockServerURLFile2;

    private static ClientAndServer mockServer;
    private static int MOCKSERVER_PORT = 3343;

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        mockServer = new ClientAndServer(MOCKSERVER_PORT);
        mockServerURL = new URL("http://localhost:" + MOCKSERVER_PORT + "/test.txt");
        mockServerURLFile2 = new URL("http://localhost:" + MOCKSERVER_PORT + "/test2.txt");
    }

    @AfterClass
    public static void tearDown() {
        mockServer.stop();
    }

    @Test
    public void testGetDownloadFileFileIsDownloaded() throws Exception {
        mockServer.when(request().withMethod("GET").withPath("/test.txt")).respond(response().withStatusCode(200)
                .withHeaders(new Header("Content-Type", "application/config")).withBody("Content file to download"));

        File temporaryFile = File.createTempFile("test", "txt");
        temporaryFile.deleteOnExit();
        new Downloader(new FileSizeUtilities()).get(mockServerURL, temporaryFile, e -> {
        });

        String fileContent = IOUtils.toString(new FileReader(temporaryFile));

        assertEquals("Content file to download", fileContent);
    }

    @Test
    public void testGetDownloadFileInAStringFileIsDownloaded() throws Exception {
        mockServer.when(request().withMethod("GET").withPath("/test2.txt")).respond(response().withStatusCode(200)
                .withHeaders(new Header("Content-Type", "application/config")).withBody("Content file to download 2"));

        String result = new Downloader(new FileSizeUtilities()).get(mockServerURLFile2, e -> {
        });

        assertEquals("Content file to download 2", result);
    }
}