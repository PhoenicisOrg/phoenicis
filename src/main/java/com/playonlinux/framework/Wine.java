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

package com.playonlinux.framework;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDefaultDirectorySize;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.core.scripts.ScriptClass;
import com.playonlinux.core.log.ProcessLogger;
import com.playonlinux.core.services.manager.ServiceException;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.utils.ExeAnalyser;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.core.version.Version;
import com.playonlinux.engines.wine.WineDistribution;
import com.playonlinux.wine.WineException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.playonlinux.core.lang.Localisation.translate;
import static java.lang.String.format;

@Scan
@ScriptClass
@SuppressWarnings("unused")
public class Wine {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager backgroundServicesManager;

    private static final Logger LOGGER = Logger.getLogger(Wine.class);
    private static final Architecture DEFAULT_ARCHITECTURE = Architecture.I386;
    private static final long NEWPREFIXSIZE = 320_000_000;
    private static final String DEFAULT_DISTRIBUTION_NAME = "staging";
    private static final String OVERWRITE = "Overwrite (usually works, no guarantee)";
    private static final String ERASE = "Erase (virtual drive content will be lost)";
    private static final String ABORT = "Abort installation";

    private final SetupWizard setupWizard;

    private com.playonlinux.wine.WinePrefix prefix;
    private String prefixName;
    private WineInstallation wineInstallation;

    public int getLastReturnCode() {
        return lastReturnCode;
    }

    private int lastReturnCode = -1;

    public Wine(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    /**
     * Select the prefix. If the prefix already exists, this method load its parameters and the prefix will be set as
     * initialized.
     * @param prefixName the name of the prefix
     * @return the same object
     */
    public Wine selectPrefix(String prefixName) throws CancelException {
        this.prefixName = prefixName;
        try {
            this.prefix = new com.playonlinux.wine.WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));
        } catch (WineException e) {
            throw new ScriptFailureException(e);
        }

        if(prefix.initialized()) {
            wineInstallation = new WineInstallation(prefix.fetchVersion(), prefix.fetchDistribution(), setupWizard);
            if(!wineInstallation.isInstalled()) {
                wineInstallation.install();
            }
        }

        return this;
    }

    public Wine createPrefix(String version) throws CancelException {
        return this.createPrefix(version, DEFAULT_DISTRIBUTION_NAME);
    }


    /**
     * Create the prefix and load its parameters. The prefix will be set as initialized
     * @param version version of wine
     * @return the same object
     * @throws CancelException if the prefix cannot be created or if the user cancels the operation
     */
    public Wine createPrefix(String version, String distribution) throws CancelException {
        return this.createPrefix(version, distribution, DEFAULT_ARCHITECTURE.name());
    }

    /**
     * Create the prefix and load its parameters. The prefix will be set as initialized
     * @param version version of wine
     * @param architecture architecture of wine
     * @return the same object
     * @throws CancelException if the prefix cannot be created or if the user cancels the operation
     */
    public Wine createPrefix(String version, String distribution, String architecture) throws CancelException {
        if(prefix == null) {
            throw new ScriptFailureException("Prefix must be selected!");
        }

        if(prefix.exists()) {
            log("Prefix already exists");
            try {
                switch (setupWizard.menu(
                        translate(format("The target virtual drive %s already exists:", prefixName)),
                        Arrays.asList(OVERWRITE, ERASE, ABORT)
                )) {
                    case OVERWRITE:
                        log("User choice: OVERWRITE");
                        return this;
                    case ERASE:
                        log("User choice: ERASE");
                        prefix.delete();
                        break;
                    case ABORT:
                        log("User choice: ABORT");
                        throw new CancelException("The script was aborted");
                }
            } catch (IOException e) {
                throw new ScriptFailureException(e);
            }
        }

        wineInstallation = new WineInstallation(new Version(version), new WineDistribution(
                OperatingSystem.fetchCurrentOperationSystem(),
                Architecture.valueOf(architecture),
                distribution
        ), setupWizard);

        if(!wineInstallation.isInstalled()) {
            wineInstallation.install();
        }

        final ProgressControl progressControl = this.setupWizard.progressBar(
                format(
                        translate("Please wait while the virtual drive is being created..."), prefixName
                )
        );

        try(ObservableDefaultDirectorySize observableDirectorySize = new ObservableDefaultDirectorySize(prefix.getWinePrefixDirectory(), 0,
                NEWPREFIXSIZE)) {
            observableDirectorySize.setCheckInterval(10);
            observableDirectorySize.addObserver(progressControl);
            backgroundServicesManager.register(observableDirectorySize);
            Process process = wineInstallation.getInstallation().createPrefix(this.prefix);
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                process.destroy();
                killall();
                throw new CancelException(e);
            }
        } catch (IllegalStateException | ServiceInitializationException e) {
            throw new ScriptFailureException(e);
        } catch (WineException e) {
            throw new ScriptFailureException("Unable to createPrefix the wineprefix", e);
        }

        return this;
    }

    /**
     * Killall the processes in the prefix
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine killall() throws ScriptFailureException {
        validateWineInstallationInitialized();
        try {
            wineInstallation.getInstallation().killAllProcess(this.prefix);
        } catch (IOException logged) {
            LOGGER.warn("Unable to kill wine processes", logged);
        }

        return this;
    }

    /**
     * Run wine in the prefix
     * @return the process object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    private Process runAndGetProcess(File workingDirectory,
                                     String executableToRun,
                                     List<String> arguments,
                                     Map<String, String> environment) throws ScriptFailureException {
        validateWineInstallationInitialized();

        validateArchitecture(workingDirectory, executableToRun);
        if("regedit".equalsIgnoreCase(executableToRun)) {
            logRegFile(workingDirectory, arguments);
        }

        try {
            final Process process = wineInstallation
                    .getInstallation()
                    .run(prefix, workingDirectory, executableToRun, environment, arguments);

            if(this.setupWizard.getLogContext() != null) {
                ProcessLogger processLogger = new ProcessLogger(process, this.setupWizard.getLogContext());
                backgroundServicesManager.register(processLogger);
            }
            return process;
        } catch (ServiceException | WineException e) {
            throw new ScriptFailureException("Error while running wine:", e);
        }
    }

    private void logRegFile(File workingDirectory, List<String> pathToRegFile) throws ScriptFailureException {
        if(pathToRegFile.size() >= 1) {
            final File regFile = findFile(workingDirectory, pathToRegFile.get(0));
            if (regFile.exists() && regFile.isFile()) {
                this.log("Content of " + regFile);
                this.log("-----------");
                try {
                    this.log(FileUtils.readFileToString(regFile));
                } catch (IOException e) {
                    LOGGER.warn(e);
                }
                this.log("-----------");
            } else {
                this.log(regFile + " does not seem to be a file. Not logging");
            }
        } else {
            this.log("User manually modified the registry");
        }
    }

    private void log(String message) throws ScriptFailureException {
        setupWizard.log(message);

        if(prefix != null) {
            try {
                prefix.log(message);
            } catch (IOException e) {
                setupWizard.log("Unable to log to the wineprefix", e);
            }
        }
    }

    private void validateArchitecture(File workingDirectory, String executableToRun) {
        if(wineInstallation.getWineDistribution().getArchitecture() == Architecture.I386) {
            final File executedFile = findFile(workingDirectory, executableToRun);
            if(executedFile.exists()) {
                try {
                    if(ExeAnalyser.is64Bits(executedFile)) {
                        throw new IllegalStateException("A 32bit wineprefix cannot execute 64bits executables!");
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    private File findFile(File workingDirectory, String executableToRun) {
        if(new File(workingDirectory, executableToRun).exists()) {
            return new File(workingDirectory, executableToRun);
        } else {
            return new File(executableToRun);
        }
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(File workingDirectory, String executableToRun, List<String> arguments,
                                    Map<String, String> environment) throws ScriptFailureException {
        runAndGetProcess(workingDirectory, executableToRun, arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(File executableToRun, List<String> arguments, Map<String, String> environment)
            throws ScriptFailureException {
        File workingDirectory = executableToRun.getParentFile();
        runBackground(workingDirectory, executableToRun.getAbsolutePath(), arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(String executableToRun, List<String> arguments, Map<String, String> environment)
            throws ScriptFailureException {
        runBackground(this.prefix.getWinePrefixDirectory(), executableToRun, arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(String executableToRun, List<String> arguments) throws ScriptFailureException {
        runBackground(new File(executableToRun), arguments, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(File executableToRun, List<String> arguments) throws ScriptFailureException {
        runBackground(executableToRun, arguments, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @param executableToRun executable to run (file parameter)
     * @return the same object
     * @throws ScriptFailureException
     */
    public Wine runBackground(File executableToRun) throws ScriptFailureException {
        runBackground(executableToRun, (List<String>) null, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @param executableToRun executable to run (string parameter)
     * @return the same object
     * @throws ScriptFailureException
     */
    public Wine runBackground(String executableToRun) throws ScriptFailureException {
        runBackground(executableToRun, null, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(File workingDirectory, String executableToRun, List<String> arguments)
            throws ScriptFailureException {
        runBackground(workingDirectory, executableToRun, arguments, null);
        return this;
    }



    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runBackground(File workingDirectory, String executableToRun) throws ScriptFailureException {
        runBackground(workingDirectory, executableToRun, null, null);
        return this;
    }

    /**
     * Run wine in the prefix in foreground
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(File workingDirectory, String executableToRun, List<String> arguments,
                              Map<String, String> environment) throws CancelException {
        Process process = runAndGetProcess(workingDirectory, executableToRun, arguments, environment);
        try {
            lastReturnCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new CancelException(e);
        }
        return this;
    }

    /**
     * Run wine in the prefix in foreground
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(File executableToRun, List<String> arguments, Map<String, String> environment)
            throws CancelException {
        File workingDirectory = executableToRun.getParentFile();
        runForeground(workingDirectory, executableToRun.getAbsolutePath(), arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in foreground
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(String executableToRun, List<String> arguments, Map<String, String> environment)
            throws CancelException {
        runForeground(this.prefix.getWinePrefixDirectory(), executableToRun, arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in foreground
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(String executableToRun, List<String> arguments) throws CancelException {
        runForeground(new File(executableToRun), arguments, null);
        return this;
    }

    /**
     * Run wine in the prefix in foreground
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(File executableToRun, List<String> arguments) throws CancelException {
        runForeground(executableToRun, arguments, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @param executableToRun executable to run (file parameter)
     * @return the same object
     * @throws ScriptFailureException
     */
    public Wine runForeground(File executableToRun) throws CancelException {
        runForeground(executableToRun, (List<String>) null, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @param executableToRun executable to run (string parameter)
     * @return the same object
     * @throws ScriptFailureException
     */
    public Wine runForeground(String executableToRun) throws CancelException {
        runForeground(executableToRun, null, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(File workingDirectory, String executableToRun, List<String> arguments)
            throws CancelException {
        runForeground(workingDirectory, executableToRun, arguments, null);
        return this;
    }




    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine runForeground(File workingDirectory, String executableToRun) throws CancelException {
        runForeground(workingDirectory, executableToRun, null, null);
        return this;
    }


    /**
     * Wait for all wine application to be terminated
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public Wine waitExit() throws ScriptFailureException {
        validateWineInstallationInitialized();
        try {
            wineInstallation.getInstallation()
                    .waitAllProcesses(this.prefix);
        } catch (IOException logged) {
            LOGGER.warn("Unable to wait for wine processes", logged);
        }

        return this;
    }


    /**
     * Wait for all wine application to be terminated and createPrefix a progress bar watching for the size of a directory
     * @param directory Directory to watch
     * @param endSize Expected size of the directory when the installation is terminated
     * @return the same object
     * @throws CancelException if the users cancels or if there is any error
     */
    public Wine waitAllWatchDirectory(File directory, long endSize) throws CancelException {
        ProgressControl progressControl = this.setupWizard.progressBar(
                format(
                        translate("Please wait while the program is being installed..."), prefixName
                )
        );

        try (ObservableDefaultDirectorySize observableDirectorySize = new ObservableDefaultDirectorySize(directory, FileUtils.sizeOfDirectory(directory),
        endSize)){
            observableDirectorySize.setCheckInterval(10);
            observableDirectorySize.addObserver(progressControl);
            backgroundServicesManager.register(observableDirectorySize);
            waitExit();
        } catch (IllegalStateException | ServiceInitializationException e) {
            throw new ScriptFailureException(e);
        }


        return this;
    }

    /**
     * Delete the wineprefix
     * @return the same object
     * @throws CancelException if the users cancels or if there is any error
     */
    public Wine deletePrefix() throws CancelException {
        if(prefix.getWinePrefixDirectory().exists()) {
            ProgressControl progressControl = this.setupWizard.progressBar(
                    format(
                            translate("Please wait while the virtual drive is being deleted..."), prefixName
                    )
            );

            try(ObservableDefaultDirectorySize observableDirectorySize = new ObservableDefaultDirectorySize(prefix.getWinePrefixDirectory(), prefix.getSize(),
                    0)) {
                observableDirectorySize.setCheckInterval(10);
                observableDirectorySize.addObserver(progressControl);
                backgroundServicesManager.register(observableDirectorySize);
                prefix.delete();
            } catch (IOException | ServiceInitializationException | IllegalStateException e) {
                throw new ScriptFailureException(e);

            }
        }

        return this;
    }

    private void validateWineInstallationInitialized() throws ScriptFailureException {
        if(wineInstallation == null) {
            throw new ScriptFailureException("The prefix must be initialized before running wine");
        }
    }

}
