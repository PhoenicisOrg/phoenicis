package org.phoenicis.tools.processes;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * A wrapper class around the Java {@link Process} class allowing for a save access to the underlying process from the
 * scripts
 */
public class PhoenicisProcess {
    /**
     * The process underneath
     */
    private final Process process;

    /**
     * Constructor
     *
     * @param process The wrapped process
     */
    public PhoenicisProcess(Process process) {
        super();

        this.process = process;
    }

    /**
     * Waits for the underlying process to terminate
     *
     * @return This object
     * @throws InterruptedException thrown if the process is interrupted
     */
    public PhoenicisProcess waitFor() throws InterruptedException {
        this.process.waitFor();

        return this;
    }

    /**
     * Gets the output of the process as a String
     *
     * @return The output of the process
     * @throws IOException thrown if an error during the result fetching occurs
     */
    public String getOutput() throws IOException {
        if (this.process.isAlive()) {
            throw new IllegalStateException("The process hasn't been terminated yet (hint try \"waitFor()\")");
        }

        return IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
    }
}
