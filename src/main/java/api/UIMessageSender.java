package api;

import utils.Message;

public interface UIMessageSender {
    Object synchroneousSendAndGetResult(Message message) throws InterruptedException;
    void synchroneousSend(Message message);
}
