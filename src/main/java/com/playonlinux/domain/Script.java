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

import com.playonlinux.utils.BackgroundService;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.text.ParseException;

public abstract class Script implements BackgroundService {
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
        if("#!/bin/bash".equals(firstLine)) {
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
                    File pythonPath = new File("src/main/python");
                    System.getProperties().setProperty("python.path", pythonPath.getAbsolutePath());
                    PythonInterpreter pythonInterpreter = new PythonInterpreter();
                    executeScript(pythonInterpreter);
                } catch (PyException e) {
                    if (e.getCause() instanceof CancelException || e.getCause() instanceof InterruptedException) {
                        System.out.println("The script was canceled! "); // Fixme: better logging system
                    } else {
                        throw e;
                    }
                }
            }




        };
        scriptThread.start();

    }

    protected abstract void executeScript(PythonInterpreter pythonInterpreter);

    public abstract String extractSignature() throws ParseException, IOException;
}
