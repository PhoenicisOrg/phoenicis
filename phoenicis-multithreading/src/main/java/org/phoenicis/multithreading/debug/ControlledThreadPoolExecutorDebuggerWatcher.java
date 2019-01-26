package org.phoenicis.multithreading.debug;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.phoenicis.multithreading.ControlledThreadPoolExecutorService;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Watcher for {@link ControlledThreadPoolExecutorDebugger}
 *
 * @see ControlledThreadPoolExecutorDebugger
 */
class ControlledThreadPoolExecutorDebuggerWatcher implements Runnable {
    private final long startTime = System.currentTimeMillis();
    private final MutableBoolean running;
    private final ExecutorService[] poolsToObserve;
    private final Logger logger;
    private final int sleepTime;
    private final Map<ExecutorService, Long> lastNumberOfTasks = new HashMap<>();
    private final Map<ExecutorService, Long> lastTime = new HashMap<>();

    ControlledThreadPoolExecutorDebuggerWatcher(MutableBoolean running,
            ExecutorService[] poolsToObserve,
            Logger logger,
            int sleepTime) {
        this.running = running;
        this.poolsToObserve = poolsToObserve;
        this.logger = logger;
        this.sleepTime = sleepTime;

        for (ExecutorService poolToObserve : poolsToObserve) {
            lastNumberOfTasks.put(poolToObserve, 0L);
            lastTime.put(poolToObserve, System.currentTimeMillis());
        }
    }

    /**
     * Stops the watcher
     */
    public void stop() {
        running.setValue(false);
    }

    @Override
    public void run() {
        while (running.isTrue()) {
            for (ExecutorService executorServiceToWatch : poolsToObserve) {
                final ControlledThreadPoolExecutorService pool = (ControlledThreadPoolExecutorService) executorServiceToWatch;
                final long deltaStartTime = (System.currentTimeMillis() - startTime) / 1000;
                final long deltaLastTime = (System.currentTimeMillis() - lastTime.get(executorServiceToWatch)) / 1000;

                final long numberOfTasks = pool.getProcessed();
                final long numberOfItems = pool.getQueue().size();
                final long queueSize = numberOfItems + pool.getQueue().remainingCapacity();

                final long deltaNumberOfTasks = numberOfTasks - lastNumberOfTasks.get(executorServiceToWatch);

                logger.info(
                        String.format("[ %10s ] Done: %20s, Running: %20s, Queue: %10s, Avg: %6s t/s, Speed: %6s t/s",
                                pool.getName(),
                                numberOfTasks,
                                pool.getActiveCount(),
                                String.format("%s / %s", numberOfItems, queueSize),
                                deltaStartTime == 0 ? 0 : numberOfTasks / deltaStartTime,
                                deltaLastTime == 0 ? 0 : deltaNumberOfTasks / deltaLastTime));

                lastTime.put(executorServiceToWatch, System.currentTimeMillis());
                lastNumberOfTasks.put(executorServiceToWatch, numberOfTasks);
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
                break;
            }
        }
    }
}