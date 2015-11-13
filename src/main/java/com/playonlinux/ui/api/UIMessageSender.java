/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.ui.api;

import com.playonlinux.core.messages.Message;
import com.playonlinux.core.messages.SynchronousMessage;
import com.playonlinux.core.scripts.CancelException;

/**
 * Component that sends messages into the UI thread
 * @param <R> The return type of the message
 */
public interface UIMessageSender <R> {
    /**
     * Send a message synchronously, wait for the result and return it
     * @param message The message to pass
     * @return The result of the message
     * @throws CancelException if the users decides to cancel the message
     */
    R synchronousSendAndGetResult(SynchronousMessage<R> message) throws CancelException;

    /**
     * Sends a message, and wait for the end of its execution
     * @param message The message to send
     */
    void synchronousSend(Message message);

    /**
     * Sends a message, and don't wait for the end of its executions
     * @param message The message to send
     */
    void asynchronousSend(Message message);
}
