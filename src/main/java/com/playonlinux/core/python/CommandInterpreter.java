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

package com.playonlinux.core.python;

import java.util.function.Function;

/**
 * This is a command interpreter
 */
public interface CommandInterpreter extends AutoCloseable {
    /**
     * Send a line to the interpreter
     * 
     * @param command
     *            command to send
     * @param callback
     *            callback to run when the command is sent
     * @return true if the command is directly interpreted (complete). False if
     *         the command is incomplete (if statements, ...)
     */
    boolean sendLine(String command, Function<String, Void> callback);

    @Override
    void close();
}
