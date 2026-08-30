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

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.phoenicis.tools.files.FileSizeUtilities;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class DownloaderTest {
    private static URL mockServerURL;
    private static URL mockServerURLFile2;

    private static HttpServer httpServer;

    @BeforeClass
    public static void setUp() throws IOException {
        InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
        httpServer = HttpServer.create(new InetSocketAddress(loopbackAddress, 0), 0);
        httpServer.createContext("/test.txt", exchange -> respond(exchange, "Content file to download"));
        httpServer.createContext("/test2.txt", exchange -> respond(exchange, "Content file to download 2"));
        httpServer.start();

        int port = httpServer.getAddress().getPort();
        String loopbackHost = loopbackAddress.getHostAddress();
        mockServerURL = new URL("http", loopbackHost, port, "/test.txt");
        mockServerURLFile2 = new URL("http", loopbackHost, port, "/test2.txt");
    }

    @AfterClass
    public static void tearDown() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    private static void respond(HttpExchange exchange, String body) throws IOException {
        byte[] response = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/config");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    @Test
    public void testGetDownloadFileFileIsDownloaded() throws Exception {
        File temporaryFile = File.createTempFile("test", "txt");
        temporaryFile.deleteOnExit();
        new Downloader(new FileSizeUtilities()).get(mockServerURL, temporaryFile, e -> {
        });

        String fileContent = IOUtils.toString(new FileReader(temporaryFile));

        assertEquals("Content file to download", fileContent);
    }

    @Test
    public void testGetDownloadFileInAStringFileIsDownloaded() throws Exception {
        String result = new Downloader(new FileSizeUtilities()).get(mockServerURLFile2, e -> {
        });

        assertEquals("Content file to download 2", result);
    }
}