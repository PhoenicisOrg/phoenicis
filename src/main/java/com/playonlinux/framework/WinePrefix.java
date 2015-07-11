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
import com.playonlinux.domain.Version;
import com.playonlinux.process.ProcessLogger;
import com.playonlinux.services.BackgroundServiceManager;
import com.playonlinux.ui.ProgressStep;
import com.playonlinux.installer.CancelException;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.installer.ScriptClass;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.ObservableDirectorySize;
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.wine.WineDistribution;
import com.playonlinux.wine.WineException;
import com.playonlinux.wine.WineInstallation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.playonlinux.lang.Localisation.translate;

@Scan
@ScriptClass
@SuppressWarnings("unused")
public class WinePrefix {
    private static final Logger LOGGER = Logger.getLogger(WinePrefix.class);

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static BackgroundServiceManager backgroundServicesManager;

    private static final long NEWPREFIXSIZE = 320_000_000;
    private static final String DEFAULT_DISTRIBUTION_NAME = "staging";

    private final SetupWizard setupWizard;

    private com.playonlinux.wine.WinePrefix prefix;
    private String prefixName;
    private WineInstallation wineInstallation;
    private WineDistribution wineDistribution;

    public WinePrefix(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    /**
     * Select the prefix. If the prefix already exists, this method load its parameters and the prefix will be set as
     * initialized.
     * @param prefixName the name of the prefix
     * @return the same object
     */
    public WinePrefix select(String prefixName) throws ScriptFailureException {
        this.prefixName = prefixName;
        try {
            this.prefix = new com.playonlinux.wine.WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));
        } catch (WineException e) {
            throw new ScriptFailureException(e);
        }

        if(prefix.initialized()) {
            try {
                wineInstallation = new WineInstallation.Builder()
                        .withPath(
                                playOnLinuxContext.makeWinePath(
                                        prefix.fetchVersion(),
                                        prefix.fetchDistribution()
                                )
                        )
                        .withApplicationEnvironment(playOnLinuxContext.getSystemEnvironment())
                        .build();
            } catch (PlayOnLinuxException e) {
                LOGGER.warn("Error while detecting prefix name");
            }
        }

        return this;
    }

    public WinePrefix create(String version) throws CancelException {
        return this.create(version, DEFAULT_DISTRIBUTION_NAME);
    }


    /**
     * Create the prefix and load its parameters. The prefix will be set as initialized
     * @param version version of wine
     * @return the same object
     * @throws CancelException if the prefix cannot be created or if the user cancels the operation
     */
    public WinePrefix create(String version, String distribution) throws CancelException {
        try {
            return this.create(version, DEFAULT_DISTRIBUTION_NAME, Architecture.fetchCurrentArchitecture().name());
        } catch (CancelException e) {
            throw e;
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException("Unable to create the wineprefix", e);
        }
    }

    /**
     * Create the prefix and load its parameters. The prefix will be set as initialized
     * @param version version of wine
     * @param architecture architecture of wine
     * @return the same object
     * @throws CancelException if the prefix cannot be created or if the user cancels the operation
     */
    public WinePrefix create(String version, String distribution, String architecture) throws CancelException {
        if(prefix == null) {
            throw new ScriptFailureException("Prefix must be selected!");
        }

        try {
            wineInstallation = new WineInstallation.Builder()
                    .withPath(playOnLinuxContext.makeWinePath(
                                    new Version(version),
                                    new WineDistribution(
                                            OperatingSystem.fetchCurrentOperationSystem(),
                                            Architecture.valueOf(architecture),
                                            distribution
                                    )
                            )
                    )
                    .withApplicationEnvironment(playOnLinuxContext.getSystemEnvironment())
                    .build();
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }

        ProgressStep progressStep = this.setupWizard.progressBar(
                String.format(
                        translate("Please wait while the virtual drive is being created..."), prefixName
                )
        );
        ObservableDirectorySize observableDirectorySize;
        try {
            observableDirectorySize = new ObservableDirectorySize(prefix.getWinePrefixDirectory(), 0,
                    NEWPREFIXSIZE);
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }

        observableDirectorySize.setCheckInterval(10);
        observableDirectorySize.addObserver(progressStep);
        backgroundServicesManager.register(observableDirectorySize);

        Process process;
        try {
            process = wineInstallation.createPrefix(this.prefix);
        } catch (WineException e) {
            throw new ScriptFailureException("Unable to create the wineprefix", e);
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            process.destroy();
            killall();
            throw new CancelException(e);
        } finally {
            observableDirectorySize.deleteObserver(progressStep);
            backgroundServicesManager.unregister(observableDirectorySize);
        }


        return this;
    }

    /**
     * Killall the processes in the prefix
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix killall() throws ScriptFailureException {
        validateWineInstallationInitialized();
        try {
            wineInstallation.killAllProcess(this.prefix);
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
    private Process runAndGetProcess(File workingDirectory, String executableToRun, List<String> arguments,
                                    Map<String, String> environment) throws ScriptFailureException {
        validateWineInstallationInitialized();

        try {
            final Process process = wineInstallation.run(workingDirectory, executableToRun, environment, arguments);
            if(this.setupWizard.getLogContext() != null) {
                ProcessLogger processLogger = new ProcessLogger(process, this.setupWizard.getLogContext());
                backgroundServicesManager.register(processLogger);
            }
            return process;
        } catch (IOException e) {
            throw new ScriptFailureException("Error while running wine:" + e);
        }
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix runBackground(File workingDirectory, String executableToRun, List<String> arguments,
                                    Map<String, String> environment) throws ScriptFailureException {
        runAndGetProcess(workingDirectory, executableToRun, arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix runBackground(File executableToRun, List<String> arguments, Map<String, String> environment)
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
    public WinePrefix runBackground(String executableToRun, List<String> arguments, Map<String, String> environment)
            throws ScriptFailureException {
        runBackground(this.prefix.getWinePrefixDirectory(), executableToRun, arguments, environment);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix runBackground(File executableToRun, List<String> arguments) throws ScriptFailureException {
        runBackground(executableToRun, arguments, null);
        return this;
    }

    public WinePrefix runBackground(File executableToRun) throws ScriptFailureException {
        runBackground(executableToRun, (List<String>) null, null);
        return this;
    }

    public WinePrefix runBackground(String executableToRun) throws ScriptFailureException {
        runBackground(executableToRun, null, null);
        return this;
    }


    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix runBackground(File workingDirectory, String executableToRun, List<String> arguments)
            throws ScriptFailureException {
        runBackground(workingDirectory, executableToRun, arguments, null);
        return this;
    }

    /**
     * Run wine in the prefix in background
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix runBackground(File workingDirectory, String executableToRun) throws ScriptFailureException {
        runBackground(workingDirectory, executableToRun, null, null);
        return this;
    }


    /**
     * Wait for all wine application to be terminated
     * @return the same object
     * @throws ScriptFailureException if the wine prefix is not initialized
     */
    public WinePrefix waitAll() throws ScriptFailureException {
        validateWineInstallationInitialized();
        try {
            wineInstallation.waitAllProcesses(this.prefix);
        } catch (IOException logged) {
            LOGGER.warn("Unable to wait for wine processes", logged);
        }

        return this;
    }

    /**
     * Wait for all wine application to be terminated and create a progress bar watching for the size of a directory
     * @param directory Directory to watch
     * @param endSize Expected size of the directory when the installation is terminated
     * @return the same object
     * @throws CancelException if the users cancels or if there is any error
     */
    public WinePrefix waitAllWatchDirectory(File directory, long endSize) throws CancelException {
        ObservableDirectorySize observableDirectorySize;
        ProgressStep progressStep = this.setupWizard.progressBar(
                String.format(
                        translate("Please wait while the program is being installed..."), prefixName
                )
        );

        try {
            observableDirectorySize = new ObservableDirectorySize(directory, FileUtils.sizeOfDirectory(directory),
                    endSize);
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }

        observableDirectorySize.setCheckInterval(10);
        observableDirectorySize.addObserver(progressStep);
        backgroundServicesManager.register(observableDirectorySize);

        try {
            waitAll();
        } finally {
            observableDirectorySize.deleteObserver(progressStep);
            backgroundServicesManager.unregister(observableDirectorySize);
        }


        return this;
    }

    /**
     * Delete the wineprefix
     * @return the same object
     * @throws CancelException if the users cancels or if there is any error
     */
    public WinePrefix delete() throws CancelException {
        if(prefix.getWinePrefixDirectory().exists()) {
            ProgressStep progressStep = this.setupWizard.progressBar(
                    String.format(
                            translate("Please wait while the virtual drive is being deleted..."), prefixName
                    )
            );

            ObservableDirectorySize observableDirectorySize;
            try {
                observableDirectorySize = new ObservableDirectorySize(prefix.getWinePrefixDirectory(), prefix.getSize(),
                        0);
            } catch (PlayOnLinuxException e) {
                throw new ScriptFailureException(e);
            }

            observableDirectorySize.setCheckInterval(10);
            observableDirectorySize.addObserver(progressStep);
            backgroundServicesManager.register(observableDirectorySize);

            try {
                prefix.delete();
            } catch (IOException e) {
                throw new ScriptFailureException("Unable to delete the wineprefix", e);
            } finally {
                observableDirectorySize.deleteObserver(progressStep);
                backgroundServicesManager.unregister(observableDirectorySize);
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
