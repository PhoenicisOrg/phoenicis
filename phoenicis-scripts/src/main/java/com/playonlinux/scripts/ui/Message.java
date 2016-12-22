package com.playonlinux.scripts.ui;

import com.playonlinux.scripts.interpreter.ScriptException;

import java.util.concurrent.Semaphore;

public class Message<T> {
    private T message;
    private final Semaphore semaphore = new Semaphore(0);
    private Thread senderThread;

    void block() {
        senderThread = Thread.currentThread();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new ScriptException(e);
        }
    }

    public void send(T message) {
        this.message = message;
        semaphore.release();
    }

    public void sendCancelSignal() {
        senderThread.interrupt();
    }

    T get() {
        if(Thread.currentThread().isInterrupted()) {
            throw new ScriptException("The script was interrupted");
        }
        return message;
    }
}
