package org.phoenicis.library;

import org.phoenicis.library.dto.ShortcutDTO;

import java.util.List;

/**
 * Interface for all shortcut reader implementations
 */
public interface ShortcutReader {
    /**
     * Sets the shortcut
     *
     * @param shortcut The shortcut
     */
    void of(ShortcutDTO shortcut);

    /**
     * Returns the name of the container belonging to the shortcut
     *
     * @return The container name
     */
    String getContainer();

    /**
     * Runs a shortcut with the given user arguments
     *
     * @param arguments The user arguments
     */
    void run(List<String> arguments);

    /**
     * Stops the running shortcut
     */
    void stop();

    /**
     * Uninstalls the shortcut
     */
    void uninstall();
}
