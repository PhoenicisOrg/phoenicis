package org.phoenicis.tools.lnk;

import java.util.Optional;

/**
 * Parsed immutable Lng String Data structure
 */
class LnkStringData {
    private final Optional<String> nameString;
    private final Optional<String> relativePath;
    private final Optional<String> workingDirectory;
    private final Optional<String> commandLineArguments;
    private final Optional<String> iconLocation;

    LnkStringData(Optional<String> nameString, Optional<String> relativePath, Optional<String> workingDirectory,
            Optional<String> commandLineArguments, Optional<String> iconLocation) {
        this.nameString = nameString;
        this.relativePath = relativePath;
        this.workingDirectory = workingDirectory;
        this.commandLineArguments = commandLineArguments;
        this.iconLocation = iconLocation;
    }

    /**
     * Fetches NAME_STRING
     * @return
     */
    Optional<String> getNameString() {
        return nameString;
    }

    /**
     * Fetches RELATIVE_PATH
     * @return
     */
    Optional<String> getRelativePath() {
        return relativePath;
    }

    /**
     * Fetches WORKING_DIR
     * @return
     */
    Optional<String> getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Fetches COMMAND_LINE_ARGUMENTS
     * @return
     */
    Optional<String> getCommandLineArguments() {
        return commandLineArguments;
    }

    /**
     * Fetches ICON_LOCATION
     * @return
     */
    Optional<String> getIconLocation() {
        return iconLocation;
    }
}
