package utils;

import scripts.CancelException;

public abstract class CancelableMessage<ResultType> extends Message {
    private Boolean processCanceled = false;

    public abstract void execute(CancelableMessage message);

    public void execute(Message message) {
        this.execute((CancelableMessage) message);
    }

    public ResultType getResponse() throws InterruptedException, CancelException {
        ResultType response = (ResultType) super.getResponse();

        if(this.processCanceled) {
            throw new CancelException();
        }

        return response;
    }

    public void setCancel() {
        this.processCanceled = true;
        super.semaphore.release();
    }
}
