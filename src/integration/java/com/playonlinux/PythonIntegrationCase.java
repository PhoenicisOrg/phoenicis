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

package com.playonlinux;

import junit.framework.Test;
import junit.framework.TestResult;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.util.List;

public class PythonIntegrationCase implements Test {
    private final String methodName;
    private final String className;
    private final PythonInterpreter pythonInterpreter;

    int numberOfTest = 0;

    public PythonIntegrationCase(PythonInterpreter pythonInterpreter, String className, String methodName) {
        numberOfTest++;
        this.pythonInterpreter = pythonInterpreter;
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public int countTestCases() {
        return numberOfTest;
    }

    @Override
    public void run(TestResult testResult) {
        testResult.startTest(this);
        try {
            pythonInterpreter.exec("suite = unittest.TestSuite()");
            pythonInterpreter.exec("suite.addTest(" + className + "(\"" + methodName + "\"))");

            pythonInterpreter.exec("runner = unittest.TextTestRunner()");
            pythonInterpreter.exec("results = runner.run(suite)");
            Boolean wasSuccessFull =
                    ((PyBoolean) pythonInterpreter.eval("results.wasSuccessful()")).getBooleanValue();

            PyList failures = ((PyList) pythonInterpreter.eval("results.failures"));
            PyList errors = ((PyList) pythonInterpreter.eval("results.errors"));

            StringBuilder stacktrace = new StringBuilder();
            for(Object pyListItem: failures) {
                for(Object result: (PyTuple) pyListItem) {
                    stacktrace.append(result.toString()).append("\n");
                }
            }

            for(Object pyListItem: errors) {
                for(Object result: (PyTuple) pyListItem) {
                    stacktrace.append(result.toString()).append("\n");
                }
            }

            if (!wasSuccessFull) {
                testResult.addError(this, new Exception(stacktrace.toString()));
            }
            pythonInterpreter.cleanup();
        } catch (PyException e) {
            testResult.addError(this, e.getCause());
        } catch (Throwable e) {
            testResult.addError(this, e);
        } finally {
            testResult.endTest(this);
        }
    }

    @Override
    public String toString() {
        return className+"_"+methodName;
    }
}
