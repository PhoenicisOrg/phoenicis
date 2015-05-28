/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.wine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// FIXME: Should not be any injection at this stage

public class WineProcessBuilder {
    private Map<String, String> applicationEnvironment;
    private List<String> command;
    private File workingDirectory;
    private Map<String, String> environment;

    public static void mergeEnvironmentVariables(Map<String, String> environmentSource,
                                                  Map<String, String> environmentDestination, String environmentVariable) {
        if(environmentSource == null) {
            return;
        }

        if(environmentSource.containsKey(environmentVariable)) {
            environmentDestination.put(environmentVariable, environmentDestination.get(environmentVariable) + ":"
                    + environmentSource.get(environmentVariable));
        }
    }

    public WineProcessBuilder withCommand(List<String> command) {
        this.command = command;
        return this;
    }

    public WineProcessBuilder withApplicationEnvironment(Map<String, String> applicationEnvironment) {
        this.applicationEnvironment = applicationEnvironment;
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

    Process build() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command).directory(workingDirectory);

        Map<String, String> processEnvironement = processBuilder.environment();
        if(environment != null) {
            for (String environementVariable : environment.keySet()) {
                processEnvironement.put(environementVariable, environment.get(environementVariable));
            }
        }

        Map<String,String> systemEnvironment = System.getenv();

        mergeEnvironmentVariables(systemEnvironment, processEnvironement, "PATH");
        mergeEnvironmentVariables(systemEnvironment, processEnvironement, "LD_LIBRARY_PATH");
        mergeEnvironmentVariables(systemEnvironment, processEnvironement, "DYLD_LIBRARY_PATH");

        mergeEnvironmentVariables(applicationEnvironment, processEnvironement, "PATH");
        mergeEnvironmentVariables(applicationEnvironment, processEnvironement, "LD_LIBRARY_PATH");
        mergeEnvironmentVariables(applicationEnvironment, processEnvironement, "DYLD_LIBRARY_PATH");

        processBuilder.inheritIO();

        return processBuilder.start();
    }

}
