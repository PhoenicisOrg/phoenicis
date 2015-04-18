package ui.impl;

import javafx.application.Platform;
import scripts.CancelException;
import utils.AsynchroneousMessage;
import utils.SynchroneousMessage;
import api.UIMessageSender;

import java.util.concurrent.CountDownLatch;

public class JavaFXMessageSenderImplementation implements UIMessageSender {
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
    public Object synchroneousSendAndGetResult(SynchroneousMessage message) throws InterruptedException, CancelException {
        JavaFXMessageSenderImplementation.runAndWait(message);
        return message.getResponse();
    }

    public void synchroneousSend(SynchroneousMessage message){
        JavaFXMessageSenderImplementation.runAndWait(message);
    }

    @Override
    public void asynchroneousSend(AsynchroneousMessage message) {
        Platform.runLater(message);
    }
}
