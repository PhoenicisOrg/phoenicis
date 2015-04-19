package utils.messages;

public interface Message extends Runnable {
    void execute(Message message);
}
