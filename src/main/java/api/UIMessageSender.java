package api;

import scripts.CancelException;
import utils.AsynchroneousMessage;
import utils.SynchroneousMessage;

public interface UIMessageSender {
    Object synchroneousSendAndGetResult(SynchroneousMessage message) throws InterruptedException, CancelException;
    void synchroneousSend(SynchroneousMessage message);

    void asynchroneousSend(AsynchroneousMessage message);
}
