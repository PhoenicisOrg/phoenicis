package utils;

public abstract class InterrupterAsynchroneousMessage extends AsynchroneousMessage implements CancelerMessage {
    Thread messageSender;

    public abstract void execute(InterrupterAsynchroneousMessage message);

    public void execute(AsynchroneousMessage message) {
        this.execute((InterrupterAsynchroneousMessage) message);
    }
    public InterrupterAsynchroneousMessage() {
        this.messageSender = Thread.currentThread();
    }

    public void sendCancelSignal() {
        messageSender.interrupt();
    }
}
