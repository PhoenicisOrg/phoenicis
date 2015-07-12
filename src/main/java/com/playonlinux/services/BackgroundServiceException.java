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

package com.playonlinux.services;

import com.playonlinux.app.PlayOnLinuxException;


public class BackgroundServiceException extends PlayOnLinuxException {
    private static final String DEFAULT_MESSAGE = "Unable to initialize the background service";

    public BackgroundServiceException() {
        super(DEFAULT_MESSAGE);
    }
    public BackgroundServiceException(String message) {
        super(message);
    }
    public BackgroundServiceException(String message, Throwable parent) {
        super(message, parent);
    }
    public BackgroundServiceException(Throwable parent) {
        super(DEFAULT_MESSAGE, parent);
    }

}
