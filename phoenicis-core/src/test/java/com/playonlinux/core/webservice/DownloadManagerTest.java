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

package com.playonlinux.core.webservice;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import org.junit.Test;

public class DownloadManagerTest {

    @Test
    public void testSubmit() throws DownloadException, InterruptedException {
        HTTPDownloader httpDownloaderMock = mock(HTTPDownloader.class);
        Consumer<Exception> callBackError = mock(Consumer.class);
        Semaphore lock = new Semaphore(0);

        when(httpDownloaderMock.getBytes()).thenReturn("Download result".getBytes());

        DownloadManager downloadManager = new DownloadManager();
        downloadManager.init();


        downloadManager.submit(httpDownloaderMock, parameter -> {
            lock.release();
        }, callBackError);


        lock.acquire();
        downloadManager.shutdown();

        verify(httpDownloaderMock).getBytes();

    }
}