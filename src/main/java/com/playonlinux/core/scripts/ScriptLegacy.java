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

package com.playonlinux.core.scripts;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.utils.FileAnalyser;
import com.playonlinux.framework.ScriptFailureException;
import org.apache.commons.lang.StringUtils;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;

public class ScriptLegacy extends Script {
    private static final String BEGIN_PGP_KEY_BLOCK_LINE = "-----BEGIN PGP SIGNATURE-----";
    private static final String END_PGP_KEY_BLOCK_LINE = "-----END PGP SIGNATURE-----";
    
    @Inject
    static ScriptFactory scriptFactory;


    protected ScriptLegacy(String script, ExecutorService executorService) {
        super(script, executorService);
    }

    @Override
    protected void executeScript(PythonInterpreter pythonInterpreter) throws ScriptFailureException {
        // FIXME: Use the properties here
        Script playonlinuxBashInterpreter;
        File bashScriptFile;
        try {
            playonlinuxBashInterpreter = scriptFactory
                    .createInstance(new File("src/main/python/PlayOnLinuxBashInterpreter.py"));

            bashScriptFile = File.createTempFile("script", "sh");
            bashScriptFile.deleteOnExit();
            FileWriter bashScriptWriter = new FileWriter(bashScriptFile);
            bashScriptWriter.write(this.getScriptContent());
            bashScriptWriter.close();
        } catch (IOException | InstallerException e) {
            throw new ScriptFailureException(e);
        }


        pythonInterpreter.set("__scriptToWrap__", bashScriptFile.getAbsolutePath());

        playonlinuxBashInterpreter.executeScript(pythonInterpreter);
    }

    @Override
    public String extractContent() throws ParseException, IOException {
        return extract(false);
    }

    @Override
    public String extractSignature() throws ParseException, IOException {
        return extract(true).trim();
    }

    private String extract(boolean extractSignature) throws IOException, ParseException {
        BufferedReader bufferReader = new BufferedReader(new StringReader(this.getScriptContent()));
        StringBuilder signatureBuilder = new StringBuilder();
        String separator = FileAnalyser.identifyLineDelimiter(this.getScriptContent());

        Boolean insideSignature = false;
        for(String readLine = bufferReader.readLine(); readLine != null; readLine = bufferReader.readLine()) {
            if(readLine.contains(BEGIN_PGP_KEY_BLOCK_LINE)) {
                insideSignature = true;
            }

            if(!(extractSignature ^ insideSignature)) {
                signatureBuilder.append(readLine);
                signatureBuilder.append(separator);
            }

            if(readLine.contains(END_PGP_KEY_BLOCK_LINE)) {
                insideSignature = false;
            }
        }

        final String extractedContent = signatureBuilder.toString();

        if(StringUtils.isBlank(extractedContent)) {
            if(extractSignature) {
                throw new ParseException("The script has no valid signature!", 0);
            } else {
                throw new ParseException("The script has no valid content", 0);
            }
        }


        return extractedContent;
    }


}
