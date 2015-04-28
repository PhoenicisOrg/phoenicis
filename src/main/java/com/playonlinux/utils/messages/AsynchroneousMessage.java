package com.playonlinux.utils.messages;

public abstract class AsynchroneousMessage implements Message {

    public void run() {
        this.execute(this);
    }
}
