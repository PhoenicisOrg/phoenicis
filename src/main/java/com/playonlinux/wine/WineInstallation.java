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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineInstallation {
    private static final String WINEPREFIXCREATE_COMMAND = "wineboot";
    private static final String WINEPREFIX_ENV = "WINEPREFIX";

    private final File binaryPath;
    private final File libraryPath;
    private final Map<String, String> applicationEnvironment;

    private WineInstallation(WineInstallation.Builder builder) {
        this.binaryPath = new File(builder.path, "bin");
        this.libraryPath = new File(builder.path, "lib");
        this.applicationEnvironment = builder.applicationEnvironment;
    }



    // TODO
    public String fetchVersion() {
        return null;
    }

    private File fetchWineExecutablePath() {
        return new File(binaryPath, "wine");
    }

    private File fetchWineServerExecutablePath() {
        return new File(binaryPath, "wineserver");
    }

    // FIXME: Maybe it would be great to create a class to handle environment issues
    private void addPathInfoToEnvironment(Map<String, String> environment) {
        environment.put("PATH", this.binaryPath.getAbsolutePath());
        environment.put("LD_LIBRARY_PATH", this.libraryPath.getAbsolutePath());
    }

    public Process run(File workingDirectory, String executableToRun, Map<String, String> environment, ArrayList<String> arguments) throws IOException {
        ArrayList<String> command = new ArrayList<>();
        command.add(this.fetchWineExecutablePath().getAbsolutePath());
        command.add(executableToRun);
        if(arguments != null) {
            command.addAll(arguments);
        }

        Map<String, String> wineEnvironment = new HashMap<>();
        if(environment != null) {
            wineEnvironment.putAll(environment);
        }

        this.addPathInfoToEnvironment(wineEnvironment);

        return new WineProcessBuilder()
                .withCommand(command)
                .withEnvironment(wineEnvironment)
                .withWorkingDirectory(workingDirectory)
                .withApplicationEnvironment(applicationEnvironment)
                .build();
    }

    public Process run(File workingDirectory, String executableToRun, Map<String, String> environment) throws IOException {
        return this.run(workingDirectory, executableToRun, environment, null);
    }

    public Process createPrefix(WinePrefix winePrefix) throws IOException {
        Map<String, String> winePrefixEnvironment = new HashMap<>();
        winePrefixEnvironment.put(WINEPREFIX_ENV, winePrefix.getAbsolutePath());

        return this.run(winePrefix.getWinePrefixDirectory(), WINEPREFIXCREATE_COMMAND, winePrefixEnvironment);
    }

    public void killAllProcess(WinePrefix winePrefix) throws IOException {

        Map<String, String> environment = new HashMap<>();
        this.addPathInfoToEnvironment(environment);
        environment.put(WINEPREFIX_ENV, winePrefix.getAbsolutePath());

        List<String> command = new ArrayList<>();
        command.add(this.fetchWineServerExecutablePath().getAbsolutePath());
        command.add("-k");

        new WineProcessBuilder()
                .withCommand(command)
                .withEnvironment(environment)
                .build();
    }

    public static class Builder {
        private File path;
        private Map<String, String> applicationEnvironment;

        public WineInstallation.Builder withPath(File path) {
            this.path = path;
            return this;
        }

        public WineInstallation.Builder withApplicationEnvironment(Map<String, String> applicationEnvironment) {
            this.applicationEnvironment = applicationEnvironment;
            return this;
        }

        public WineInstallation build() {
            return new WineInstallation(this);
        }

    }



}
