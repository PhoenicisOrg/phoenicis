package org.phoenicis.tools.processes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A process builder to be used from inside the scripts
 */
public class PhoenicisProcessBuilder {
    /**
     * The command executed by the process
     */
    private List<String> command;

    /**
     * The base path where the process is executed in
     */
    private String basePath;

    /**
     * The environment variables
     */
    private Map<String, String> environment;

    /**
     * An optional path where the output is written to
     */
    private String outputPath;

    /**
     * Constructor
     */
    public PhoenicisProcessBuilder() {
        super();
    }

    public PhoenicisProcessBuilder withCommand(final List<String> command) {
        this.command = command;

        return this;
    }

    public PhoenicisProcessBuilder withBasePath(final String basePath) {
        this.basePath = basePath;

        return this;
    }

    public PhoenicisProcessBuilder withEnvironment(final Map<String, String> environment) {
        this.environment = environment;

        return this;
    }

    public PhoenicisProcessBuilder withOutputPath(final String outputPath) {
        this.outputPath = outputPath;

        return this;
    }

    /**
     * Starts a new Process with the previously specified values
     *
     * @return The newly started process
     * @throws IOException Thrown if the process couldn't be started
     */
    public PhoenicisProcess start() throws IOException {
        if (this.command == null || this.command.isEmpty()) {
            throw new IllegalArgumentException("Command can't be null or empty");
        }

        if (this.basePath == null) {
            throw new IllegalArgumentException("Base path can't be null");
        }

        final File directory = new File(this.basePath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Base path is either does not exist or is no directory");
        }

        if (this.environment == null) {
            throw new IllegalArgumentException("Environment can't be null");
        }

        final ProcessBuilder processBuilder = new ProcessBuilder()
                .command(this.command)
                .directory(directory)
                .inheritIO();

        processBuilder.environment().putAll(this.environment);

        if (this.outputPath != null) {
            final File output = new File(this.outputPath);

            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(output);
        }

        final Process process = processBuilder.start();

        return new PhoenicisProcess(process);
    }
}
