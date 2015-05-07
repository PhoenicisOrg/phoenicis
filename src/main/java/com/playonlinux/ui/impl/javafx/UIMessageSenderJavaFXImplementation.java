package com.playonlinux.ui.impl.javafx;

import javafx.application.Platform;
import com.playonlinux.domain.CancelException;
import com.playonlinux.api.UIMessageSender;
import com.playonlinux.utils.messages.Message;
import com.playonlinux.utils.messages.SynchroneousMessage;

import java.util.concurrent.CountDownLatch;

public class UIMessageSenderJavaFXImplementation<RETURN_TYPE> implements UIMessageSender<RETURN_TYPE> {
    public static void runAndWait(Runnable action) {
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
    public RETURN_TYPE synchroneousSendAndGetResult(SynchroneousMessage<RETURN_TYPE> message) throws InterruptedException, CancelException {
        UIMessageSenderJavaFXImplementation.runAndWait(message);
        return message.getResponse();
    }

    public void synchroneousSend(Message message){
        UIMessageSenderJavaFXImplementation.runAndWait(message);
    }

    @Override
    public void asynchroneousSend(Message message) {
        Platform.runLater(message);
    }
}
