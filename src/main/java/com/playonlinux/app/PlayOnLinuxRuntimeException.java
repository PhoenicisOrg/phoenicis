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

package com.playonlinux.app;

import org.apache.commons.lang.ArrayUtils;

public class PlayOnLinuxRuntimeException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1055724274893863084L;
    private final String message;
    private final Throwable parent;

    public PlayOnLinuxRuntimeException(String message) {
        this(message, null);
    }

    public PlayOnLinuxRuntimeException(String message, Throwable parent) {
        super(message);
        this.message = message;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return this.message;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return (StackTraceElement[]) ArrayUtils.addAll(super.getStackTrace(), this.parent.getStackTrace());
    }
}
