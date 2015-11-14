/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.ui.impl.qt;

import com.playonlinux.core.messages.Message;
import com.playonlinux.core.messages.SynchronousMessage;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.ui.api.UIMessageSender;
import com.trolltech.qt.core.QCoreApplication;

/**
 * Implementation of the UIMessageSender for the Qt-GUI of POL.
 */
public class UIMessageSenderQtImplementation<R> implements UIMessageSender<R> {

    @Override
    public R synchronousSendAndGetResult(SynchronousMessage<R> message) throws CancelException {
        synchronousSend(message);
        return message.getResponse();
    }

    @Override
    public void synchronousSend(Message message) {
        QCoreApplication.invokeAndWait(message);
    }

    @Override
    public void asynchronousSend(Message message) {
        QCoreApplication.invokeLater(message);
    }
}
