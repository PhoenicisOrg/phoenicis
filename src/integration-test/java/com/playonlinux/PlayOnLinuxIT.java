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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.python.core.PyStringMap;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.InjectionException;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.python.JythonInterpreterFactory;
import com.playonlinux.integration.PlayOnLinuxIntegrationRunner;
import com.playonlinux.integration.PythonIntegrationCase;

@Scan
public class PlayOnLinuxIT {
    @Inject
    static JythonInterpreterFactory jythonJythonInterpreterFactory;
    private static PlayOnLinuxIntegrationRunner integrationRunner = new PlayOnLinuxIntegrationRunner();

    @BeforeClass
    public static void setUp() throws InjectionException, IOException {
        integrationRunner.initialize();
    }

    @Test
    public void jythonTests() throws InjectionException, IOException, PlayOnLinuxException {
        try {
            URL integrationResources = PlayOnLinuxIT.class.getResource("integration");
            File[] pythonFiles = new File(integrationResources.getPath()).listFiles();

            assert pythonFiles != null;
            for (File file : pythonFiles) {
                if (!"__init__.py".equals(file.getName()) && file.getName().endsWith(".py")) {
                    final PythonInterpreter pythonInterpreter = jythonJythonInterpreterFactory.createInstance();
                    pythonInterpreter.execfile(file.getAbsolutePath());
                    PyStringMap pyDictionary = (PyStringMap) pythonInterpreter.eval("globals()");

                    for (Object className : pyDictionary.keys()) {
                        if (((String) className).startsWith("Test")) {
                            PyType pyType = (PyType) pyDictionary.__getitem__((String) className);
                            List<String> methods = (List<String>) pyType.__getattr__("__dict__").invoke("keys");

                            for (Object methodName : (methods.stream().filter(s -> s.startsWith("test")).toArray())) {
                                new PythonIntegrationCase(pythonInterpreter, (String) className, (String) methodName)
                                        .run();
                            }

                        }
                    }

                    jythonJythonInterpreterFactory.close(pythonInterpreter);
                }
            }
        } finally {
            integrationRunner.close();
        }

    }

}
