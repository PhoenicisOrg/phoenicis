package com.playonlinux.wine;

import com.playonlinux.utils.Injectable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineProcessBuilder {
    @Injectable
    private static HashMap<String, String> applicationEnvironment;
    private List<String> command;
    private File workingDirectory;
    private Map<String, String> environment;

    public WineProcessBuilder withCommand(List<String> command) {
        this.command = command;
        return this;
    }

    public WineProcessBuilder withWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    public WineProcessBuilder withEnvironment(Map<String, String> environment) {
        this.environment = environment;
        return this;
    }

    private void mergeEnvironmentVariables(Map<String, String> environmentSource,
                                           Map<String, String> environmentDestination, String environmentVariable) {
        if(environmentSource == null) {
            return;
        }
        if(environmentSource.containsKey(environmentVariable)) {
            environmentDestination.put(environmentVariable, environmentDestination.get(environmentVariable) + ":"
                    + environmentSource.get(environmentVariable));
        }
    }

    Process build() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command).directory(workingDirectory);

        Map<String, String> processEnvironement = processBuilder.environment();
        if(environment != null) {
            for (String environementVariable : environment.keySet()) {
                processEnvironement.put(environementVariable, environment.get(environementVariable));
            }
        }

        Map<String,String> systemEnvironment = System.getenv();

        this.mergeEnvironmentVariables(systemEnvironment, processEnvironement, "PATH");
        this.mergeEnvironmentVariables(systemEnvironment, processEnvironement, "LD_LIBRARY_PATH");
        this.mergeEnvironmentVariables(systemEnvironment, processEnvironement, "DYLD_LIBRARY_PATH");

        this.mergeEnvironmentVariables(applicationEnvironment, processEnvironement, "PATH");
        this.mergeEnvironmentVariables(applicationEnvironment, processEnvironement, "LD_LIBRARY_PATH");
        this.mergeEnvironmentVariables(applicationEnvironment, processEnvironement, "DYLD_LIBRARY_PATH");

        return processBuilder.start();
    }

    public static void injectApplicationEnvironment(HashMap<String, String> systemEnvironment) {
        WineProcessBuilder.applicationEnvironment = systemEnvironment;
    }
}
