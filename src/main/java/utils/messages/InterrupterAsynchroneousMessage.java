package utils.messages;

public abstract class InterrupterAsynchroneousMessage extends AsynchroneousMessage implements CancelerMessage {
    Thread messageSender;

    public InterrupterAsynchroneousMessage() {
        this.messageSender = Thread.currentThread();
    }

    public void sendCancelSignal() {
        messageSender.interrupt();
    }
}
