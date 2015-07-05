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

package com.playonlinux.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogStreamFactory {
    private final Map<String, LogStream> loggers;

    public LogStreamFactory() {
        this.loggers = new HashMap<>();
    }

    synchronized public LogStream getLogger(String logContext) throws IOException {
        if(!loggers.containsKey(logContext)) {
            loggers.put(logContext, new LogStream(logContext));
        }
        return loggers.get(logContext);
    }
}
