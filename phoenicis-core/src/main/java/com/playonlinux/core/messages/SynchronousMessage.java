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

package com.playonlinux.core.messages;

import java.util.concurrent.Semaphore;

import com.playonlinux.core.scripts.CancelException;

public abstract class SynchronousMessage<R> implements Message {
    private R response;
    protected final Semaphore semaphore = new Semaphore(0);

    @Override
    public void run() {
        this.execute(this);
    }

    public R getResponse() throws CancelException {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new CancelException("The action has been interrupted", e);
        }
        return this.response;
    }

    public void setResponse(R response) {
        this.response = response;
        semaphore.release();
    }

}
