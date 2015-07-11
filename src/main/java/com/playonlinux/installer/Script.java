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

package com.playonlinux.installer;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.python.JythonInterpreterFactory;
import com.playonlinux.services.BackgroundServiceManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.python.core.PyException;

import com.playonlinux.framework.ScriptFailureException;
import com.playonlinux.services.BackgroundService;
import org.python.util.PythonInterpreter;

@Scan
public abstract class Script implements BackgroundService {
    @Inject
    private static BackgroundServiceManager backgroundServiceManager;

    @Inject
    private static JythonInterpreterFactory jythonInterpreterFactory;

    private static final Logger LOGGER = Logger.getLogger(Script.class);
    private final ExecutorService executor;
    private Future runningScript;

    private final String scriptContent;


    protected Script(String scriptContent, ExecutorService executor) {
        this.executor = executor;
        this.scriptContent = scriptContent;
    }

    public static Script.Type detectScriptType(String script) {
        String firstLine = script.split("\n")[0];
        if("#!/bin/bash".equals(firstLine) || "#!/usr/bin/env playonlinux-bash".equals(firstLine)) {
            return Script.Type.LEGACY;
        } else {
            return Script.Type.RECENT;
        }
    }


    @Override
    public void shutdown() {
        runningScript.cancel(true);
        executor.shutdown();
    }

    public String getScriptContent() {
        return scriptContent;
    }



    public enum Type {
        RECENT,
        LEGACY
    }

    @Override
    public void start() {
        runningScript = executor.submit(() -> {
            try {

                PythonInterpreter pythonInterpreter = jythonInterpreterFactory.createInstance();

                try {
                    executeScript(pythonInterpreter);
                } catch (PyException e) {
                    if (e.getCause() instanceof ScriptFailureException) {
                        LOGGER.error("The script encountered an error");
                    }
                    if (e.getCause() instanceof CancelException) {
                        LOGGER.info("The script has been canceled");
                    }
                    LOGGER.error(ExceptionUtils.getStackTrace(e));
                } catch (ScriptFailureException e) {
                    LOGGER.error("The script encountered an error", e);
                } finally {
                    LOGGER.info("Cleaning up");
                    pythonInterpreter.cleanup();
                    jythonInterpreterFactory.close(pythonInterpreter);
                    backgroundServiceManager.unregister(Script.this);
                }
            } catch(PlayOnLinuxException e) {
                LOGGER.error("Cannot create interpreter", e);
            }
        });
    }

    protected abstract void executeScript(PythonInterpreter pythonInterpreter) throws ScriptFailureException;

    public abstract String extractSignature() throws ParseException, IOException;
}
