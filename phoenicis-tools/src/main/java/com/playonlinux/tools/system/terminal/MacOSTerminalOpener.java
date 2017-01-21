package com.playonlinux.tools.system.terminal;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

class MacOSTerminalOpener implements TerminalOpener {
    @Override
    public void openTerminal(String workingDirectory, Map<String, String> environmentVariables) {
        try {
            final File temporaryScript = File.createTempFile("terminal", "sh");
            if(temporaryScript.setExecutable(true)) {
                temporaryScript.deleteOnExit();
                try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryScript)) {
                    IOUtils.write(createScript(workingDirectory, environmentVariables), fileOutputStream, "UTF-8");
                    fileOutputStream.flush();
                }

                final ProcessBuilder processBuilder = new ProcessBuilder()
                        .command("/usr/bin/open", "-b", "com.apple.terminal", temporaryScript.getAbsolutePath());

                processBuilder.start();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String createScript(String workingDirectory, Map<String, String> environmentVariables) {
        final StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder
                .append("cd \"").append(workingDirectory).append("\" \n")
                .append("[ -e \"$HOME/.profile\" ] && rcFile=\"~/.profile\" || rcFile=\"/etc/profile\"\n" );

        for(String environmentVariable: environmentVariables.keySet()) {
            scriptBuilder.append(String.format("export %s=\"%s\"\n", environmentVariable, environmentVariables.get(environmentVariable)));
        }
        return scriptBuilder.append("exec bash -c \"clear;printf '\\e[3J';bash --rcfile $rcFile\"").toString();
    }
}
