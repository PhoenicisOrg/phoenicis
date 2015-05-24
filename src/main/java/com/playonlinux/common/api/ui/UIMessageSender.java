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

package com.playonlinux.common.api.ui;

import com.playonlinux.domain.CancelException;
import com.playonlinux.common.messages.Message;
import com.playonlinux.common.messages.SynchroneousMessage;

public interface UIMessageSender <RETURN_TYPE> {
    RETURN_TYPE synchroneousSendAndGetResult(SynchroneousMessage<RETURN_TYPE> message) throws InterruptedException, CancelException;

    void synchroneousSend(Message message);

    void asynchroneousSend(Message message);
}
