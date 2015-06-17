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

package com.playonlinux.webservice;


import com.playonlinux.messages.ParametrableRunnable;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

public class DownloadManagerTest {

    @Test
    public void testSubmit() throws DownloadException, ExecutionException, InterruptedException {
        HTTPDownloader httpDownloaderMock = mock(HTTPDownloader.class);
        ParametrableRunnable<byte[]> callBackMock = mock(ParametrableRunnable.class);
        ParametrableRunnable<Exception> callBackError = mock(ParametrableRunnable.class);

        when(httpDownloaderMock.getBytes()).thenReturn("Download result".getBytes());

        DownloadManager downloadManager = new DownloadManager();
        downloadManager.start();


        downloadManager.submit(httpDownloaderMock, callBackMock, callBackError);

        downloadManager.shutdown();
        verify(httpDownloaderMock).get();
        verify(callBackMock).run();
        verify(callBackMock).setParameter("Download result".getBytes());
    }
}