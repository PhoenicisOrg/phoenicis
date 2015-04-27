package ui.javafximpl;

import javafx.application.Platform;
import scripts.CancelException;
import api.UIMessageSender;
import utils.messages.Message;
import utils.messages.SynchroneousMessage;

import java.util.concurrent.CountDownLatch;

public class JavaFXMessageSenderImplementation<ReturnType> implements UIMessageSender<ReturnType> {
    public static void runAndWait(Runnable action) {
        Object result = null;
        if (action == null)
            throw new NullPointerException("action");

        // run synchronously on JavaFX thread
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        // queue on JavaFX thread and wait for completion
        final CountDownLatch doneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                doneLatch.countDown();
            }
        });

        try {
            doneLatch.await();
        } catch (InterruptedException e) {
            // ignore exception
        }
    }

    @Override
    public ReturnType synchroneousSendAndGetResult(SynchroneousMessage<ReturnType> message) throws InterruptedException, CancelException {
        JavaFXMessageSenderImplementation.runAndWait(message);
        return message.getResponse();
    }

    public void synchroneousSend(Message message){
        JavaFXMessageSenderImplementation.runAndWait(message);
    }

    @Override
    public void asynchroneousSend(Message message) {
        Platform.runLater(message);
    }
}
