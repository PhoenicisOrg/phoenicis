/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.tools.system.terminal;

import org.apache.commons.io.IOUtils;
import org.phoenicis.configuration.security.Safe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Safe
class LinuxTerminalOpener implements TerminalOpener {

    private Optional<String> terminalCommand;

    @Override
    public void openTerminal(String workingDirectory, Map<String, String> environmentVariables) {
        try {
            final File temporaryScript = File.createTempFile("terminal", "sh");
            if (temporaryScript.setExecutable(true)) {
                temporaryScript.deleteOnExit();
                try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryScript)) {
                    IOUtils.write(createScript(workingDirectory, environmentVariables), fileOutputStream, "UTF-8");
                    fileOutputStream.flush();
                }

                final ProcessBuilder processBuilder = new ProcessBuilder().command(terminalCommand.orElse("xterm"),
                        "-e", "bash -c '" + temporaryScript.getAbsolutePath() + "'");

                processBuilder.start();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String createScript(String workingDirectory, Map<String, String> environmentVariables) {
        final StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("cd \"").append(workingDirectory).append("\"\n");

        for (Map.Entry<String, String> entry : environmentVariables.entrySet()) {
            scriptBuilder.append(String.format("export %s=\"%s\"\n", entry.getKey(), entry.getValue()));
        }
        return scriptBuilder.append("exec $SHELL").toString();
    }

    @Override
    public void setTerminalCommand(Optional<String> terminalCommand) {
        this.terminalCommand = terminalCommand;
    }
}
