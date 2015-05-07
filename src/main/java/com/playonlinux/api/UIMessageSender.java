package com.playonlinux.api;

import com.playonlinux.domain.CancelException;
import com.playonlinux.utils.messages.Message;
import com.playonlinux.utils.messages.SynchroneousMessage;

public interface UIMessageSender <RETURNTYPE> {
    RETURNTYPE synchroneousSendAndGetResult(SynchroneousMessage<RETURNTYPE> message) throws InterruptedException, CancelException;

    void synchroneousSend(Message message);

    void asynchroneousSend(Message message);
}
