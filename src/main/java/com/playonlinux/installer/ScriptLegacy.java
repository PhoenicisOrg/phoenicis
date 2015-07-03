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

package com.playonlinux.installer;

import com.playonlinux.framework.ScriptFailureException;
import com.playonlinux.python.Interpreter;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;

public class ScriptLegacy extends Script {
    private static final String BEGIN_PGP_KEY_BLOCK_LINE = "-----BEGIN PGP PUBLIC KEY BLOCK-----";
    private static final String END_PGP_KEY_BLOCK_LINE = "-----END PGP PUBLIC KEY BLOCK-----";


    protected ScriptLegacy(String script, ExecutorService executorService) {
        super(script, executorService);
    }

    @Override
    protected void executeScript(Interpreter pythonInterpreter) throws ScriptFailureException {
        // FIXME: Use the properties here
        Script playonlinuxBashInterpreter;
        File bashScriptFile;
        try {
            playonlinuxBashInterpreter =
                    new ScriptFactoryDefaultImplementation()
                            .createInstance(new File("src/main/python/PlayOnLinuxBashInterpreter.py"));

            bashScriptFile = File.createTempFile("script", "sh");
            bashScriptFile.deleteOnExit();
            FileWriter bashScriptWriter = new FileWriter(bashScriptFile);
            bashScriptWriter.write(this.getScriptContent());
            bashScriptWriter.close();
        } catch (IOException e) {
            throw new ScriptFailureException(e);
        }


        pythonInterpreter.set("__scriptToWrap__", bashScriptFile.getAbsolutePath());

        playonlinuxBashInterpreter.executeScript(pythonInterpreter);
    }

    @Override
    public String extractSignature() throws ParseException, IOException {
        BufferedReader bufferReader = new BufferedReader(new StringReader(this.getScriptContent()));
        StringBuilder signatureBuilder = new StringBuilder();

        String readLine;
        Boolean insideSignature = false;
        do {
            readLine = bufferReader.readLine();
            if(BEGIN_PGP_KEY_BLOCK_LINE.equals(readLine)) {
                insideSignature = true;
            }

            if(insideSignature) {
                signatureBuilder.append(readLine);
                signatureBuilder.append("\n");
            }

            if(END_PGP_KEY_BLOCK_LINE.equals(readLine)) {
                insideSignature = false;
            }
        } while(readLine != null);

        String signature = signatureBuilder.toString().trim();

        if(StringUtils.isBlank(signature)) {
            throw new ParseException("The script has no valid signature!", 0);
        }
        return signature;
    }
}
