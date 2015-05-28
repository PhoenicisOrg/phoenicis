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

import com.playonlinux.domain.ScriptFailureException;
import com.playonlinux.domain.ScriptTemplate;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class PythonInstallerTest {
    @Test
    public void testDefineLogContext() throws IOException, ScriptFailureException {
        File temporaryScript = File.createTempFile("testDefineLogContext", "py");
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryScript);

        fileOutputStream.write(("#!/usr/bin/env/python\n" +
                "from com.playonlinux.framework.templates import Installer\n" +
                "\n" +
                "class PlayOnLinuxBashInterpreter(Installer):\n" +
                "    def main(self):\n" +
                "        pass\n" +
                "    def defineLogContext(self):\n" +
                "        return \"Mock Log Context\"\n").getBytes());

        Interpreter interpreter = Interpreter.createInstance();
        interpreter.execfile(temporaryScript.getAbsolutePath());
        PythonInstaller<ScriptTemplate> pythonInstaller = new PythonInstaller<>(interpreter, ScriptTemplate.class);

        assertEquals("Mock Log Context", pythonInstaller.extractLogContext());
    }
}