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

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.InjectionException;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.utils.Files;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RunWith(AllTests.class)
@Scan
public class PlayOnLinuxIntegrationTest {
    @Inject static PlayOnLinuxContext playOnLinuxContext;

    public static void setUp() throws InjectionException, IOException {
        AbstractConfiguration testConfigFile = new IntegrationContextConfig();
        testConfigFile.setStrictLoadingPolicy(false);
        testConfigFile.load();

        File home = new File(playOnLinuxContext.getProperty("application.user.root"));
        Files.remove(home);
        home.mkdirs();
    }

    public static Test suite() throws InjectionException, IOException {
        setUp();
        final TestSuite suite = new TestSuite("PythonIntegrationCase");

        URL integrationResources = PlayOnLinuxIntegrationTest.class.getResource("integration");
        File[] pythonFiles = new File(integrationResources.getPath()).listFiles();

        assert pythonFiles != null;
        for(File file: pythonFiles) {
            if(!"__init__.py".equals(file.getName())) {
                final PythonInterpreter pythonInterpreter = new PythonInterpreter();
                pythonInterpreter.setOut(new NullOutputStream());
                pythonInterpreter.setErr(new NullOutputStream());
                pythonInterpreter.execfile(file.getAbsolutePath());
                PyStringMap pyDictionary = (PyStringMap) pythonInterpreter.eval("globals()");

                for(Object className: pyDictionary.keys()) {
                    if (((String) className).startsWith("Test")) {
                        PyType pyType = (PyType) pyDictionary.__getitem__((String) className);
                        List<String> methods = (List<String>) pyType.__getattr__("__dict__").invoke("keys");

                        for(Object methodName: (methods.stream().filter(s -> s.startsWith("test")).toArray())) {
                            suite.addTest(new PythonIntegrationCase(pythonInterpreter,
                                    (String) className, (String) methodName
                            ));
                        }

                    }
                }
            }
        }

        return suite;
    }

}
