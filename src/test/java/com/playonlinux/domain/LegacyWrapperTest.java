/*
 * Copyright (C) 2015 Markus Ebner
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

import org.junit.Test;
import org.python.util.PythonInterpreter;

import java.io.File;

import static org.junit.Assert.*;

public class LegacyWrapperTest {

    @Test
    public void testLegacyWrapper() throws Exception {
        File tmpFile = new File("/tmp/POL_WrapperTest");
        //ensure temporary file does not exist before running the testScript
        if(tmpFile.exists()){
            tmpFile.delete();
        }
        File testScript = new File(this.getClass().getResource("wrapperTestScript.sh").getPath());
        Script testScriptWrapper = Script.createInstance(testScript);
        //Fixme: This is rather ugly here. Create a static Helper for PythonInterpreters?
        File pythonPath = new File("src/main/python");
        System.getProperties().setProperty("python.path", pythonPath.getAbsolutePath());
        testScriptWrapper.executeScript(new PythonInterpreter());
        //file should exist now
        assertTrue(tmpFile.exists());
    }

}
