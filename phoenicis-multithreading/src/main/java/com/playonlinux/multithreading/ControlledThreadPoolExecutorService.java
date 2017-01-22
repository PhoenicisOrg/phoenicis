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

package com.playonlinux.multithreading;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ControlledThreadPoolExecutorService extends ThreadPoolExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlledThreadPoolExecutorService.class);

    private final Semaphore semaphore;
    private final String name;
    private final AtomicLong processed = new AtomicLong(0);
    private final AtomicLong remainingTasks = new AtomicLong(0);
    private final int numberOfThreads;
    private boolean shouldShutdown = false;

    public ControlledThreadPoolExecutorService(String name, int numberOfThread, int queueSize) {
        super(numberOfThread, numberOfThread, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(queueSize));
        this.semaphore = new Semaphore(queueSize);
        this.name = name;
        this.numberOfThreads = numberOfThread;
    }

    @Override
    public void execute(Runnable runnable) {
        try {
            remainingTasks.incrementAndGet();
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        super.execute(runnable);
    }

    @Override
    public void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        if(throwable != null) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(throwable));
        }
        semaphore.release();
        processed.addAndGet(1);
        if(remainingTasks.decrementAndGet() == 0) {
            if(shouldShutdown) {
                shutdown();
            }
        }
    }

    public void sendShutdownSignal() {
        if(remainingTasks.get() == 0) {
            shutdown();
        }
        shouldShutdown = true;
    }

    /**
     * Get the number of tasks the pool has processed
     * @return The number of processed tasks
     */
    long getNumberOfProcessedTasks() {
        return processed.get();
    }

    /**
     * Get the total size of the queue
     * @return the size of the queue
     */
    int getQueueSize() {
        return this.getQueue().size() + this.getQueue().remainingCapacity();
    }

    /**
     * Get the number of items in the queue
     * @return the current number of items in the queue
     */
    int getQueueNumberOfItems() {
        return this.getQueue().size() + this.numberOfThreads;
    }

    /**
     *
     * @return The name of the pool
     */
    public String getName() {
        return name;
    }
}
