package com.playonlinux.utils.messages;

import com.playonlinux.domain.CancelException;

public abstract class CancelerSynchroneousMessage<RESULT_TYPE> extends SynchroneousMessage implements CancelerMessage {
    private Boolean processCanceled = false;

    public RESULT_TYPE getResponse() throws InterruptedException, CancelException {
        RESULT_TYPE response = (RESULT_TYPE) super.getResponse();

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
