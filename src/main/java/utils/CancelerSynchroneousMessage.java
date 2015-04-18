package utils;

import scripts.CancelException;

public abstract class CancelerSynchroneousMessage<ResultType> extends SynchroneousMessage implements CancelerMessage {
    private Boolean processCanceled = false;

    public abstract void execute(CancelerSynchroneousMessage message);

    public void execute(SynchroneousMessage message) {
        this.execute((CancelerSynchroneousMessage) message);
    }

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
