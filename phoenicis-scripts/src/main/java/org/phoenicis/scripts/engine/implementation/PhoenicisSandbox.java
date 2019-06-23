package org.phoenicis.scripts.engine.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoenicisSandbox {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoenicisSandbox.class);

    public boolean isSafe(String identifier) {
        LOGGER.debug("Loading {} in javascript context", identifier);
        if (identifier.startsWith("org.phoenicis")) {
            return true;
        }

        if (identifier.startsWith("java.lang")) {
            // FIXME: This should be more fine-tuned later
            // Contains process builder
            return true;
        }

        if (identifier.startsWith("java.util")) {
            // FIXME: This should be more fine-tuned later
            return true;
        }

        // Needed by GraalVM
        if (identifier.startsWith("java.net.URLClassLoader")) {
            return true;
        }

        return false;
    }
}
