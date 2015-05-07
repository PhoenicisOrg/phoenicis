package com.playonlinux.utils.messages;

import com.playonlinux.domain.CancelException;

public abstract class CancelerSynchroneousMessage<RESULTTYPE> extends SynchroneousMessage implements CancelerMessage {
    private Boolean processCanceled = false;

    public RESULTTYPE getResponse() throws InterruptedException, CancelException {
        RESULTTYPE response = (RESULTTYPE) super.getResponse();

        if(this.processCanceled) {
            throw new CancelException();
        }

        return response;
    }

    public void sendCancelSignal() {
        this.processCanceled = true;
        super.semaphore.release();
    }
}
