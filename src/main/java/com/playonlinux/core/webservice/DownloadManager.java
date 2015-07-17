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

import com.playonlinux.core.messages.ParametrableRunnable;
import com.playonlinux.core.services.SubmitableService;
import com.playonlinux.core.services.manager.AutoStartedService;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 Represents a download manager
 */
@AutoStartedService
public class DownloadManager implements
        SubmitableService<HTTPDownloader, ParametrableRunnable<byte[]>> {
    private static final int DEFAULT_POOL_SIZE = 4;
    private static final int DEFAULT_QUEUE_SIZE = 2000;
    private final ThreadPoolExecutor threadPoolExecutor;
    private static final Logger LOGGER = Logger.getLogger(DownloadManager.class);

    public DownloadManager() {
        this(DEFAULT_POOL_SIZE, DEFAULT_QUEUE_SIZE);
    }

    public DownloadManager(int poolSize, int queueSize) {
        BlockingQueue<Runnable> queue
                = new ArrayBlockingQueue<>(queueSize);

        threadPoolExecutor = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                Long.MAX_VALUE,
                TimeUnit.DAYS,
                queue
        );

    }
    @Override
    public void shutdown() {
        threadPoolExecutor.shutdownNow();
    }

    @Override
    public void start() {
        // Nothing to start here
    }

    @Override
    public void submit(HTTPDownloader task, ParametrableRunnable<byte[]> callback,
                       ParametrableRunnable<Exception> error) {


        threadPoolExecutor.submit(() -> {
            try {
                byte[] downloadResult = task.getBytes();
                callback.setParameter(downloadResult);
                callback.run();
            } catch (DownloadException e) {
                LOGGER.error(e);
                error.setParameter(e);
                error.run();
            }
        });
    }

    public void submit(URL url, ParametrableRunnable<byte[]> callback,
                       ParametrableRunnable<Exception> error) {
        HTTPDownloader task = new HTTPDownloader(url);
        submit(task, callback, error);
    }
}
