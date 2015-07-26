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

import com.playonlinux.framework.ScriptFailureException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.log.LogStream;
import com.playonlinux.core.log.LogStreamFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.python.core.*;
import org.python.util.PythonInterpreter;
import org.reflections.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

@Scan
public class PythonInstaller<T> extends AbstractPythonModule<T> {
    @Inject
    static LogStreamFactory logStreamFactory;

    private static final String MAIN_METHOD_NAME = "main";
    private static final String DEFINE_LOGCONTEXT_NAME = "title";
    private static final java.lang.String ROLLBACK_METHOD_NAME = "rollback";
    private static final java.lang.String DEFAULT_ROLLBACK_METHOD_NAME = "_defaultRollback";
    private PyObject mainInstance;
    private static final Logger LOGGER = Logger.getLogger(PythonInstaller.class);

    public PythonInstaller(PythonInterpreter pythonInterpreter, Class<T> type) {
        super(pythonInterpreter, type);
    }

    public PyType getMainClass() {
        return this.getCandidateClasses().get(0);
    }

    private PyObject getMainInstance() {
        if(mainInstance == null) {
            mainInstance = this.getMainClass().__call__();
        }
        return mainInstance;
    }

    public boolean hasMain() {
        return !(this.getCandidateClasses().isEmpty());
    }

    public void runMain(PyObject mainInstance) {
        mainInstance.invoke(MAIN_METHOD_NAME);
    }

    public String extractLogContext() throws ScriptFailureException {
        return (String) extractAttribute(DEFINE_LOGCONTEXT_NAME);
    }

    private void injectAllPythonAttributes() throws ScriptFailureException {
        Class <?> parentClass = ((PyType) ((PyType) getMainClass().getBase()).getBase()).getProxyType();

        final Set<Field> fields = ReflectionUtils.getAllFields(parentClass,
                ReflectionUtils.withAnnotation(PythonAttribute.class));

        for(Field field: fields) {
            field.setAccessible(true);
            try {
                field.set(getMainInstance().__tojava__(type), this.extractAttribute(field.getName()));
            } catch (IllegalAccessException e) {
                throw new ScriptFailureException(e);
            }
        }
    }

    public void exec() throws ScriptFailureException {
        if(this.hasMain()) {
            String logContext = this.extractLogContext();
            LogStream logStream = null;

            if(logContext != null) {
                try {
                    logStream = logStreamFactory.getLogger(logContext);
                    pythonInterpreter.setOut(logStream);
                } catch (IOException e) {
                    throw new ScriptFailureException(e);
                }
            }

            this.injectAllPythonAttributes();

            try {
                this.runMain(getMainInstance());
            } catch(Exception e) {
                LOGGER.error("The script encountered an error. Rolling back");
                try {
                    getMainInstance().invoke(ROLLBACK_METHOD_NAME);
                } catch (Exception rollbackException) {
                    getMainInstance().invoke(DEFAULT_ROLLBACK_METHOD_NAME);
                    rollbackException.initCause(e);
                    throw rollbackException;
                }
                throw e;
            } finally {
                if(logStream != null) {
                    try {
                        logStream.flush();
                        logStream.close();
                    } catch (IOException e) {
                        LOGGER.warn("Unable to flush script log stream", e);
                    }
                }
            }
        }
    }



    public Object extractAttribute(String attributeToExtract) {
        PyObject pyLogAttribute;
        try {
            pyLogAttribute = getMainInstance().__getattr__(attributeToExtract);
        } catch (PyException e) {
            LOGGER.info(String.format("The attribute %s was not found. Returning null", attributeToExtract), e);
            return null;
        }
        if (pyLogAttribute instanceof PyMethod) {
            PyObject pyReturn = getMainInstance().invoke(attributeToExtract);
            if (pyReturn != null && !(pyReturn instanceof PyNone)) {
                return pyReturn.__tojava__(Object.class);
            } else {
                return null;
            }
        } else {
            return pyLogAttribute.__tojava__(Object.class);
        }
    }
}
