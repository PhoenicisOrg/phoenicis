package com.playonlinux.api;

import com.playonlinux.domain.CancelException;
import com.playonlinux.utils.messages.Message;
import com.playonlinux.utils.messages.SynchroneousMessage;

public interface UIMessageSender <RETURN_TYPE> {
    RETURN_TYPE synchroneousSendAndGetResult(SynchroneousMessage<RETURN_TYPE> message) throws InterruptedException, CancelException;

    void synchroneousSend(Message message);

    void asynchroneousSend(Message message);
}
