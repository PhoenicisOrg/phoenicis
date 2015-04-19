package utils.messages;

public abstract class InterrupterSynchroneousMessage extends SynchroneousMessage implements CancelerMessage {
    Thread messageSender;

    public InterrupterSynchroneousMessage() {
        this.messageSender = Thread.currentThread();
    }

    public void sendCancelSignal() {
        messageSender.interrupt();
    }
}
