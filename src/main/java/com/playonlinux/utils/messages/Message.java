package com.playonlinux.utils.messages;

public interface Message extends Runnable {
    void execute(Message message);
}
