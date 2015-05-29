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

package com.playonlinux.python;

import com.playonlinux.common.log.LogStream;
import com.playonlinux.domain.ScriptFailureException;
import org.python.core.*;

import java.io.IOException;

public class PythonInstaller<T> extends AbstractPythonModule<T> {
    private static final String MAIN_METHOD_NAME = "main";
    private static final String DEFINE_LOGCONTEXT_NAME = "logContext";

    public PythonInstaller(Interpreter pythonInterpreter, Class<T> type) {
        super(pythonInterpreter, type);
    }

    public PyType getMainClass() {
        return this.getCandidateClasses().get(0);
    }

    private PyObject getMainInstance() {
        return this.getMainClass().__call__();
    }

    public boolean hasMain() {
        return this.getCandidateClasses().size() > 0;
    }

    public void runMain() {
        this.getMainInstance().invoke(MAIN_METHOD_NAME);
    }

    public String extractLogContext() throws ScriptFailureException {
        return extractStringAttribute(DEFINE_LOGCONTEXT_NAME);
    }

    public void exec() throws ScriptFailureException {
        if(this.hasMain()) {
            String logContext = this.extractLogContext();
            if(logContext != null) {
                try {
                    pythonInterpreter.setOut(new LogStream(logContext));
                } catch (IOException e) {
                    throw new ScriptFailureException(e);
                }
            }
            this.runMain();
        }
    }

    public String extractStringAttribute(String attributeToExtract) throws ScriptFailureException {
        PyObject pyLogAttribute = this.getMainInstance().__getattr__(attributeToExtract);
        if (pyLogAttribute instanceof PyString) {
            return ((PyString) pyLogAttribute).getString();
        } else {
            PyObject pyLogContext = this.getMainInstance().invoke(attributeToExtract);
            if (pyLogContext != null && !(pyLogContext instanceof PyNone)) {
                if (!(pyLogContext instanceof PyString)) {
                    throw new ScriptFailureException(String.format("%s must return a string.", attributeToExtract));
                } else {
                    return ((PyString) pyLogContext).getString();
                }
            } else {
                return null;
            }
        }
    }
}
