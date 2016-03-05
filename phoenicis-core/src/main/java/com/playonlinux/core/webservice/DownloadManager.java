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

import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.playonlinux.core.services.SubmittableService;

import lombok.extern.slf4j.Slf4j;

/*
 Represents a download manager
 */
@Slf4j
public class DownloadManager implements SubmittableService<HTTPDownloader, Consumer<byte[]>> {
    private static final int DEFAULT_POOL_SIZE = 4;
    private static final int DEFAULT_QUEUE_SIZE = 2000;
    private final ThreadPoolExecutor threadPoolExecutor;

    public DownloadManager() {
        this(DEFAULT_POOL_SIZE, DEFAULT_QUEUE_SIZE);
    }

    public DownloadManager(int poolSize, int queueSize) {
        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);

        threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, Long.MAX_VALUE, TimeUnit.DAYS, queue);

    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdownNow();
    }

    @Override
    public void init() {
        // Nothing to start here
    }

    @Override
    public void submit(HTTPDownloader task, Consumer<byte[]> callback, Consumer<Exception> error) {

        threadPoolExecutor.submit(() -> {
            try {
                byte[] downloadResult = task.getBytes();
                callback.accept(downloadResult);
            } catch (DownloadException e) {
                log.error("Failed to submit download", e);
                error.accept(e);
            }
        });
    }

    public void submit(URL url, Consumer<byte[]> callback, Consumer<Exception> error) {
        HTTPDownloader task = new HTTPDownloader(url);
        submit(task, callback, error);
    }
}
