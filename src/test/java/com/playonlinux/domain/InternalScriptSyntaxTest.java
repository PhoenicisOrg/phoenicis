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

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.python.util.PythonInterpreter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

public class InternalScriptSyntaxTest {

    @Test
    public void testInternalScripts() throws Exception {
        File pythonDir = new File("src/main/python");
        Iterator<File> scripts = FileUtils.iterateFiles(pythonDir, new String[]{"py"}, true);

        while(scripts.hasNext()){
            File scriptFile = scripts.next();
            Reader scriptReader = new FileReader(scriptFile);

            PythonInterpreter interpreter = new PythonInterpreter();
            try{
                interpreter.compile(scriptReader);
            } finally {
                interpreter.cleanup();
            }
        }
    }

}
