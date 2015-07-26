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

package com.playonlinux.core.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Access to the logger from any PlayOnLinux class
 */
public class LoggerFactory {
    private final Map<String, ScriptLogger> scriptLoggers;
    private final Map<String, WinePrefixLogger> winePrefixLoggers;

    public LoggerFactory() {
        this.scriptLoggers = new HashMap<>();
        this.winePrefixLoggers = new HashMap<>();
    }

    /**
     * Get a script logger
     * @param logContext (usually the name of the script)
     * @return the logger (an outputstream)
     * @throws IOException if the file cannot be opened for any reason
     */
    public synchronized ScriptLogger getScriptLogger(String logContext) throws IOException {
        if(!scriptLoggers.containsKey(logContext)) {
            scriptLoggers.put(logContext, new ScriptLogger(logContext));
        }
        return scriptLoggers.get(logContext);
    }

    /**
     * Get a wineprefix logger
     * @param prefixName the name of the prefix
     * @return the logger (an outputstream)
     * @throws IOException if the file cannot be opened for any reason
     */
    public synchronized WinePrefixLogger getWinePrefixLogger(String prefixName) throws IOException {
        if(!winePrefixLoggers.containsKey(prefixName)) {
            winePrefixLoggers.put(prefixName, new WinePrefixLogger(prefixName));
        }
        return winePrefixLoggers.get(prefixName);
    }

    public synchronized void close(ScriptLogger scriptLogger) throws IOException {
        scriptLogger.flush();
        scriptLogger.close();
        scriptLoggers.values().remove(scriptLogger);
    }

    public synchronized void close(WinePrefixLogger winePrefixLogger) throws IOException {
        winePrefixLogger.flush();
        winePrefixLogger.close();
        scriptLoggers.values().remove(winePrefixLogger);
    }


}
