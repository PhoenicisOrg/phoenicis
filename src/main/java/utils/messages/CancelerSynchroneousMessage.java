package utils.messages;

import scripts.CancelException;

public abstract class CancelerSynchroneousMessage<ResultType> extends SynchroneousMessage implements CancelerMessage {
    private Boolean processCanceled = false;

    public ResultType getResponse() throws InterruptedException, CancelException {
        ResultType response = (ResultType) super.getResponse();

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
