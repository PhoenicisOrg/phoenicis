package com.playonlinux.multithreading;


import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

public class ControlledThreadPoolExecutorServiceCloser implements AutoCloseable {
    private final ControlledThreadPoolExecutorService[] executorServices;
    private boolean closeImmediately = false;

    ControlledThreadPoolExecutorServiceCloser(ControlledThreadPoolExecutorService... executorServices) {
        this.executorServices = executorServices;
    }

    public void setCloseImmediately(boolean closeImmediately) {
        this.closeImmediately = closeImmediately;
    }

    @PreDestroy
    @Override
    public void close() throws InterruptedException {
        for (ControlledThreadPoolExecutorService executorService : executorServices) {
            executorService.sendShutdownSignal();
            if(!closeImmediately) {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            }
            executorService.shutdownNow();
        }
    }
}
