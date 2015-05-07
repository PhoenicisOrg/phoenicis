package com.playonlinux.utils.messages;

import com.playonlinux.domain.CancelException;

import java.util.concurrent.Semaphore;

public abstract class SynchroneousMessage<RESULTTYPE> implements Message {
    private RESULTTYPE response;
    Semaphore semaphore = new Semaphore(0);

    public void run() {
        this.execute(this);
    }

    public RESULTTYPE getResponse() throws InterruptedException, CancelException {
        semaphore.acquire();
        return this.response;
    }

    public void setResponse(RESULTTYPE response) {
        this.response = response;
        semaphore.release();
    }

}
