package utils;

import scripts.CancelException;

import java.util.concurrent.Semaphore;

public abstract class SynchroneousMessage<ResultType> implements Runnable {
    private ResultType response;
    Semaphore semaphore = new Semaphore(0);

    abstract public void execute(SynchroneousMessage message);

    public void run() {
        this.execute(this);
    }

    public ResultType getResponse() throws InterruptedException, CancelException {
        semaphore.acquire();
        return this.response;
    }

    public void setResponse(ResultType response) {
        this.response = response;
        semaphore.release();
    }

}
