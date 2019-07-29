package org.phoenicis.tools.processes;

import org.phoenicis.configuration.security.Safe;

@Safe
public class ProcessUtils {

    /**
     * Creates a new {@link PhoenicisProcessBuilder} object
     *
     * @return The new PhoenicisProcessBuilder object
     */
    public PhoenicisProcessBuilder newBuilder() {
        return new PhoenicisProcessBuilder();
    }
}
