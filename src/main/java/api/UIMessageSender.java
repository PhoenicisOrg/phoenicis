package api;

import scripts.CancelException;
import utils.messages.Message;
import utils.messages.SynchroneousMessage;

public interface UIMessageSender {
    Object synchroneousSendAndGetResult(SynchroneousMessage message) throws InterruptedException, CancelException;

    void synchroneousSend(Message message);

    void asynchroneousSend(Message message);
}
