package org.phoenicis.multithreading.debug;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.phoenicis.configuration.security.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;

/**
 * This class allows to run a debugger that will show indications about
 * It will print out the following information
 */
@Safe
public class ControlledThreadPoolExecutorDebugger implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlledThreadPoolExecutorDebugger.class);
    private static final int REFRESH_TIME = 5000;

    private final ExecutorService executorService;
    private final ExecutorService[] poolsToObserve;

    private MutableBoolean running = new MutableBoolean(true);
    private ControlledThreadPoolExecutorDebuggerWatcher watcher;

    /**
     *
     * @param executorService The {@link ExecutorService that will watch the pools to be observed}
     * @param poolsToObserve The {@link ExecutorService} of the pools to be observed
     * @see ExecutorService
     */
    public ControlledThreadPoolExecutorDebugger(ExecutorService executorService,
            ExecutorService... poolsToObserve) {
        this.poolsToObserve = poolsToObserve;
        this.executorService = executorService;
    }

    /**
     * Starts the debugger
     */
    public void start() {
        if (watcher == null) {
            watcher = new ControlledThreadPoolExecutorDebuggerWatcher(running, poolsToObserve, LOGGER, REFRESH_TIME);
            executorService.submit(watcher);
        }
    }

    /**
     * Stops the debugger
     */
    public void stop() {
        if (watcher != null) {
            watcher.stop();
            watcher = null;
        }
    }

    @PreDestroy
    @Override
    public void close() {
        stop();
        executorService.shutdownNow();
        running.setValue(false);
    }
}