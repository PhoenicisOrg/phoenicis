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

package com.phoenicis.multithreading;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ControlledThreadPoolExecutorServiceCloserTest {
    private ControlledThreadPoolExecutorService mock1 = mock(ControlledThreadPoolExecutorService.class);
    private ControlledThreadPoolExecutorService mock2 = mock(ControlledThreadPoolExecutorService.class);
    private ControlledThreadPoolExecutorServiceCloser controlledThreadPoolExecutorCloser =
            new ControlledThreadPoolExecutorServiceCloser(mock1, mock2);

    @Test
    public void testClose() throws InterruptedException {
        controlledThreadPoolExecutorCloser.close();
        verify(mock1).sendShutdownSignal();
        verify(mock1).awaitTermination(anyLong(), any());
        verify(mock1).shutdownNow();

        verify(mock2).sendShutdownSignal();
        verify(mock2).awaitTermination(anyLong(), any());
        verify(mock2).shutdownNow();
    }
}