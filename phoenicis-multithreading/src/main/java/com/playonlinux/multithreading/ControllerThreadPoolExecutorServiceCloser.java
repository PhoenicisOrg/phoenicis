package com.playonlinux.multithreading;


import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

class ControllerThreadPoolExecutorServiceCloser implements AutoCloseable {
    private final ControlledThreadPoolExecutorService[] executorServices;

    ControllerThreadPoolExecutorServiceCloser(ControlledThreadPoolExecutorService... executorServices) {
        this.executorServices = executorServices;
    }

    @PreDestroy
    @Override
    public void close() throws InterruptedException {
        for(ControlledThreadPoolExecutorService executorService: executorServices) {
            executorService.sendShutdownSignal();
            executorService.awaitTermination(1, TimeUnit.HOURS);
            executorService.shutdownNow();
        }
    }
}
