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

import com.playonlinux.MockContextConfig;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.framework.templates.ScriptTemplate;

import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.InjectionException;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.python.util.PythonInterpreter;

import java.io.*;

import static org.junit.Assert.*;

public class PythonInstallerTest {
    private DefaultJythonJythonInterpreterFactory defaultJythonInterpreterFactory;
    private static AbstractConfiguration testConfigFile = new MockContextConfig();

    @BeforeClass
    public static void setUpClass() throws InjectionException {
        testConfigFile.setStrictLoadingPolicy(false);
        testConfigFile.load();
    }

    @Before
    public void setUp() {
        defaultJythonInterpreterFactory = new DefaultJythonJythonInterpreterFactory();
    }

    @Test
    public void testPythonInstaller_DefineLogContextWithMethod_ContextIsSet() throws IOException, PlayOnLinuxException {
        File temporaryScript = File.createTempFile("testDefineLogContext", "py");
        temporaryScript.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryScript);

        fileOutputStream.write(("#!/usr/bin/env/python\n" +
                "from com.playonlinux.framework.templates import Installer\n" +
                "\n" +
                "class PlayOnLinuxBashInterpreter(Installer):\n" +
                "    def main(self):\n" +
                "        pass\n" +

                "    def title(self):\n" +
                "        return \"Mock Log Context\"\n").getBytes());

        PythonInterpreter interpreter = defaultJythonInterpreterFactory.createInstance();
        interpreter.execfile(temporaryScript.getAbsolutePath());
        PythonInstaller<ScriptTemplate> pythonInstaller = new PythonInstaller<>(interpreter, ScriptTemplate.class);

        assertEquals("Mock Log Context", pythonInstaller.extractLogContext());
    }


    @Test
    public void testPythonInstaller_DefineLogContextWithAttribute_ContextIsSet() throws IOException, PlayOnLinuxException {
        File temporaryScript = File.createTempFile("testDefineLogContext", "py");
        temporaryScript.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryScript);

        fileOutputStream.write(("#!/usr/bin/env/python\n" +
                "from com.playonlinux.framework.templates import Installer\n" +
                "\n" +
                "class PlayOnLinuxBashInterpreter(Installer):\n" +
                "   title = \"Mock Log Context 2\"\n" +
                "   def main(self):\n" +
                "        pass\n").getBytes());

        PythonInterpreter interpreter = defaultJythonInterpreterFactory.createInstance();
        interpreter.execfile(temporaryScript.getAbsolutePath());
        PythonInstaller<ScriptTemplate> pythonInstaller = new PythonInstaller<>(interpreter, ScriptTemplate.class);

        assertEquals("Mock Log Context 2", pythonInstaller.extractLogContext());
    }


    @Test
    public void testPythonInstaller_DefineVariableAttributes_AttributesAreSet() throws IOException, PlayOnLinuxException {
        File temporaryOutput = new File("/tmp/testPythonInstaller_DefineVariableAttributes.log");
        temporaryOutput.deleteOnExit();

        if(temporaryOutput.exists()) {
            temporaryOutput.delete();
        }

        File temporaryScript = File.createTempFile("defineVariableAttributes", "py");
        temporaryScript.deleteOnExit();

        FileOutputStream fileOutputStream = new FileOutputStream(temporaryScript);

        fileOutputStream.write(("from com.playonlinux.framework.templates import MockWineSteamInstaller\n" +
                "\n" +
                "class Example(MockWineSteamInstaller):\n" +
                "    title = \"testPythonInstaller_DefineVariableAttributes\"\n" +
                "    prefix = \"Prefix\"\n" +
                "    wineversion = \"1.7.34\"\n" +
                "    steamId = 130\n" +
                "    packages = [\"package1\", \"package2\"]\n").getBytes());

        PythonInterpreter interpreter = defaultJythonInterpreterFactory.createInstance();
        interpreter.execfile(temporaryScript.getAbsolutePath());
        PythonInstaller<ScriptTemplate> pythonInstaller = new PythonInstaller<>(interpreter, ScriptTemplate.class);

        pythonInstaller.exec();

        assertTrue(FileUtils.readFileToString(temporaryOutput).contains("Implementation has to be done, but we have access to prefix (Prefix), " +
                "wineversion (1.7.34), steamId (130) and packages (['package1', 'package2'])." +
                " First package (to check that we have a list: package1\n"));
    }

    @AfterClass
    public static void tearDownClass() throws InjectionException {
        testConfigFile.close();
    }
}