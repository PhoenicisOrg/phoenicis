/*
 * Copyright (c) 2016. Ce code a été écrit dans le cadre d'un projet industriel en majeure Système
 * d'Informations à Supélec.
 * Auteurs:
 *    - Yoan THIEBAULT (yoanthiebault@gmail.com)
 *    - Quentin PÂRIS (contact@qparis.fr)
 *    - Antoine CROO
 *
 * Encadrant:
 *    - Marc-Antoine WEISSER
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.playonlinux.multithreading;

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