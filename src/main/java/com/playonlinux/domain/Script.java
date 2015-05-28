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

package com.playonlinux.domain;

import com.playonlinux.common.api.services.BackgroundService;
import com.playonlinux.python.Interpreter;
import org.apache.log4j.Logger;
import org.python.core.PyException;

import java.io.*;
import java.text.ParseException;

public abstract class Script implements BackgroundService {
    static Logger logger = Logger.getLogger(Script.class);

    private Thread scriptThread;
    private File scriptFile;

    protected Script(File scriptFile) {
        this.scriptFile = scriptFile;
    }


    @Override
    public void shutdown() {
        scriptThread.interrupt();
    }

    public File getScriptFile() {
        return scriptFile;
    }

    public enum Type {
        RECENT,
        LEGACY
    }

    public static Script createInstance(File script) throws IOException {
        switch(Script.detectScriptType(script)) {
            case LEGACY:
                return new ScriptLegacy(script);
            case RECENT:
            default:
                return new ScriptRecent(script);
        }
    }

    public static Type detectScriptType(File script) throws IOException {
        BufferedReader bufferReader = new BufferedReader(new FileReader(script));
        String firstLine = bufferReader.readLine();
        if("#!/bin/bash".equals(firstLine) || "#!/usr/bin/env playonlinux-bash".equals(firstLine)) {
            return Type.LEGACY;
        } else {
            return Type.RECENT;
        }
    }


    @Override
    public void start() {
        scriptThread = new Thread() {
            @Override
            public void run() {
                try {
                    executeInterpreter();
                } catch (PyException e) {
                    if(e.getCause() instanceof ScriptFailureException) {
                        logger.error("The script encountered an error");
                    }
                    if(e.getCause() instanceof CancelException) {
                        logger.info("The script has been canceled");
                    }
                    logger.error(e);
                    logger.error(e.getCause());
                } catch (ScriptFailureException e) {
                    logger.error(e);
                }
            }
        };
        scriptThread.start();

    }

    public void executeInterpreter() throws ScriptFailureException {
        Interpreter pythonInterpreter = new Interpreter();
        executeScript(pythonInterpreter);
    }

    protected abstract void executeScript(Interpreter pythonInterpreter) throws ScriptFailureException;

    public abstract String extractSignature() throws ParseException, IOException;
}
