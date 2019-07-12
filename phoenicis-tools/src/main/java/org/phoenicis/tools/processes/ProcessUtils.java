package org.phoenicis.tools.processes;

import org.phoenicis.configuration.security.Safe;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Safe
public class ProcessUtils {

    /**
     * Starts a new system process with the given command
     *
     * @param command     The command to be executed
     * @param basePath    The location from where the command is executed
     * @param environment A map of environemnt variables
     * @param outputPath  An optional path where the output should be saved
     * @return The created process
     * @throws IOException thrown if the process couldn't be started
     */
    public PhoenicisProcess startProcess(List<String> command, String basePath, Map<String, String> environment, String outputPath) throws IOException {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Command can't be null or empty");
        }

        if (basePath == null) {
            throw new IllegalArgumentException("Base path can't be null");
        }

        final File directory = new File(basePath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Base path is either does not exist or is no directory");
        }

        if (environment == null) {
            throw new IllegalArgumentException("Environment can't be null");
        }

        final ProcessBuilder processBuilder = new ProcessBuilder()
                .command(command)
                .directory(directory)
                .inheritIO();

        processBuilder.environment().putAll(environment);

        if (outputPath != null) {
            final File output = new File(outputPath);

            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(output);
        }

        final Process process = processBuilder.start();

        return new PhoenicisProcess(process);
    }

}
