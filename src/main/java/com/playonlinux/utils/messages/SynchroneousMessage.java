package com.playonlinux.utils.messages;

import com.playonlinux.scripts.CancelException;

import java.util.concurrent.Semaphore;

public abstract class SynchroneousMessage<ResultType> implements Message {
    private ResultType response;
    Semaphore semaphore = new Semaphore(0);

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
