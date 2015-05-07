package com.playonlinux.utils.messages;

import com.playonlinux.domain.CancelException;

import java.util.concurrent.Semaphore;

public abstract class SynchroneousMessage<RESULT_TYPE> implements Message {
    private RESULT_TYPE response;
    Semaphore semaphore = new Semaphore(0);

    public void run() {
        this.execute(this);
    }

    public RESULT_TYPE getResponse() throws InterruptedException, CancelException {
        semaphore.acquire();
        return this.response;
    }

    public void setResponse(RESULT_TYPE response) {
        this.response = response;
        semaphore.release();
    }

}
