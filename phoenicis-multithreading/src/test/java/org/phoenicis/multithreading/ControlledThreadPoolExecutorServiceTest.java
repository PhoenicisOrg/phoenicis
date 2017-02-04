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

package org.phoenicis.multithreading;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ControlledThreadPoolExecutorServiceTest {
    @Test
    public void testSubmitTask_taskAreExecuted() throws InterruptedException {
        final ControlledThreadPoolExecutorService controlledThreadPoolExecutor = new ControlledThreadPoolExecutorService("test", 4, 2);
        final AtomicInteger atomicInteger = new AtomicInteger(0);

        for(int i = 0; i < 100; i++) {
            controlledThreadPoolExecutor.submit(() -> atomicInteger.accumulateAndGet(1, (left, right) -> left + right));
        }
        controlledThreadPoolExecutor.shutdown();
        controlledThreadPoolExecutor.awaitTermination(1, TimeUnit.DAYS);

        assertEquals(100, atomicInteger.get());

        controlledThreadPoolExecutor.shutdownNow();
    }

    @Test
    public void testGetName() {
        final ControlledThreadPoolExecutorService controlledThreadPoolExecutor = new ControlledThreadPoolExecutorService("test", 4, 2);

        assertEquals("test", controlledThreadPoolExecutor.getName());
    }

    @Test
    public void testInitialState() {
        final ControlledThreadPoolExecutorService controlledThreadPoolExecutor = new ControlledThreadPoolExecutorService("test", 10, 20);

        assertEquals(20, controlledThreadPoolExecutor.getQueueSize());
        assertEquals(0, controlledThreadPoolExecutor.getNumberOfProcessedTasks());
        assertEquals(10, controlledThreadPoolExecutor.getQueueNumberOfItems());
    }
}