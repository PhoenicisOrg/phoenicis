package utils;

public abstract class AsynchroneousMessage implements Runnable {
    abstract public void execute(AsynchroneousMessage message);

    public void run() {
        this.execute(this);
    }

}
