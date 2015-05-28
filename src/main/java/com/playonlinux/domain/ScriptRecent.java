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

package com.playonlinux.domain;

import com.playonlinux.python.Interpreter;
import com.playonlinux.python.PythonInstaller;
import org.apache.commons.lang.StringUtils;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class ScriptRecent extends Script {
    protected ScriptRecent(File script) {
        super(script);
    }

    @Override
    protected void executeScript(Interpreter pythonInterpreter) throws ScriptFailureException {
        pythonInterpreter.execfile(this.getScriptFile().getAbsolutePath());
        PythonInstaller<ScriptTemplate> pythonInstaller = new PythonInstaller<>(pythonInterpreter, ScriptTemplate.class);

        if(pythonInstaller.hasMain()) {
            String logContext = pythonInstaller.extractLogContext();
            if(logContext != null) {
                System.out.println("Log context: " + logContext);
            }
            pythonInstaller.runMain();
        }
    }



    @Override
    public String extractSignature() throws ParseException, IOException {
        BufferedReader bufferReader = new BufferedReader(new FileReader(this.getScriptFile()));
        StringBuilder signatureBuilder = new StringBuilder();

        String readLine;
        Boolean insideSignature = false;
        do {
            readLine = bufferReader.readLine();
            if(readLine == null) {
                break;
            }
            if(readLine.contains("-----BEGIN PGP PUBLIC KEY BLOCK-----") && readLine.startsWith("#")) {
                insideSignature = true;
            }

            if(insideSignature) {
                if(readLine.startsWith("#")) {
                    signatureBuilder.append(readLine.substring(1, readLine.length()).trim());
                }
                signatureBuilder.append("\n");
            }

            if(readLine.contains("-----END PGP PUBLIC KEY BLOCK-----") && readLine.startsWith("#")) {
                insideSignature = false;
            }
        } while(true);

        String signature = signatureBuilder.toString().trim();

        if(StringUtils.isBlank(signature)) {
            throw new ParseException("The script has no valid signature!", 0);
        }
        return signature;
    }
}
