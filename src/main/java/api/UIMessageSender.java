package api;

import scripts.CancelException;
import utils.Message;

public interface UIMessageSender {
    Object synchroneousSendAndGetResult(Message message) throws InterruptedException, CancelException;
    void synchroneousSend(Message message);
}
