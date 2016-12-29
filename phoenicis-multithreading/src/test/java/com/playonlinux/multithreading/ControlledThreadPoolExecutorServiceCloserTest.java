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

import java.util.concurrent.ExecutorService;

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