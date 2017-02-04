/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.phoenicis.scripts.ui;

import com.phoenicis.scripts.interpreter.ScriptException;

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
