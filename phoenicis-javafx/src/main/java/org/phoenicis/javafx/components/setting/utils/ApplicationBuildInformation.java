package org.phoenicis.javafx.components.setting.utils;

public class ApplicationBuildInformation {
    /**
     * The name of the application
     */
    private final String applicationName;
    /**
     * The version of the application (taken from the maven pom file)
     */
    private final String applicationVersion;
    /**
     * The git revision/commit used to build Phoenicis
     */
    private final String applicationGitRevision;
    /**
     * The timestamp when Phoenicis was built
     */
    private final String applicationBuildTimestamp;

    /**
     * Constructor
     *
     * @param applicationName the name of the application
     * @param applicationVersion the version of the application
     * @param applicationGitRevision the git revision/commit used to build Phoenicis
     * @param applicationBuildTimestamp the timestamp when Phoenicis was built
     */
    public ApplicationBuildInformation(String applicationName, String applicationVersion,
            String applicationGitRevision, String applicationBuildTimestamp) {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationGitRevision = applicationGitRevision;
        this.applicationBuildTimestamp = applicationBuildTimestamp;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getApplicationGitRevision() {
        return applicationGitRevision;
    }

    public String getApplicationBuildTimestamp() {
        return applicationBuildTimestamp;
    }
}
