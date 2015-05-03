package com.playonlinux.ui.impl.mockui;

import com.playonlinux.api.UIMessageSender;
import com.playonlinux.utils.CancelException;
import com.playonlinux.utils.messages.Message;
import com.playonlinux.utils.messages.SynchroneousMessage;

public class MockUIMessageSenderImplementation<T> implements UIMessageSender<T> {
    @Override
    public T synchroneousSendAndGetResult(SynchroneousMessage<T> message) throws InterruptedException, CancelException {
        message.run();
        return message.getResponse();
    }

    @Override
    public void synchroneousSend(Message message) {
        message.run();
    }

    @Override
    public void asynchroneousSend(Message message) {
        new Thread(message).start();
    }
}
