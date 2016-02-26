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

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.log4j.Logger;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.config.ConfigFile;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.core.scripts.ScriptClass;
import com.playonlinux.core.scripts.ScriptFailureException;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.streams.ProcessPipe;
import com.playonlinux.core.streams.TeeOutputStream;
import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.ExeAnalyser;
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.core.version.Version;
import com.playonlinux.engines.wine.WineDistribution;
import com.playonlinux.filesystem.DirectoryWatcherSize;
import com.playonlinux.framework.wizard.SetupWizardComponent;
import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.wine.WineException;
import com.playonlinux.wine.WinePrefix;
import com.playonlinux.wine.registry.AbstractRegistryNode;
import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryValue;
import com.playonlinux.wine.registry.RegistryWriter;
import com.playonlinux.wine.registry.StringValueType;

@Scan
@ScriptClass
public class Wine implements SetupWizardComponent {
	private static final Logger LOGGER = Logger.getLogger(Wine.class);
	
	private static final String EXCEPTION_PREFIX_INCOMPATIBLE_ARCH = "A 32bit wineprefix cannot execute 64bits executables!";
	private static final String EXCEPTION_PREFIX_NOT_INITIALIZED = "The prefix must be initialized before running wine";
	private static final String EXCEPTION_PREFIX_NOT_SELECTED = "Prefix must be selected!";
	private static final String EXCEPTION_SCRIPT_ABORTED = "The script was aborted";
	private static final String EXCEPTION_WINE_ERROR = "Error while running wine:";

	private static final String I18N_PROGRAM_INSTALLING = "Please wait while the program is being installed...";
	private static final String I18N_VIRTUAL_DRIVE_EXIST = "The target virtual drive %s already exists:";
	private static final String I18N_VIRTUAL_DRIVE_CREATION = "Please wait while the virtual drive is being created...";
	private static final String I18N_VIRTUAL_DRIVE_DELETION = "Please wait while the virtual drive is being deleted...";
	
	private static final String LOG_LOGGING_WINEPREFIX_FAILED = "Unable to log to the wineprefix";
	private static final String LOG_MANUALLY_MODIFIED_REGISTRY = "User manually modified the registry";
	private static final String LOG_NOT_A_FILE = " does not seem to be a file. Not logging";
	private static final String LOG_LINE_SEPARATOR = "-----------";
	private static final String LOG_CONTENT_OF = "Content of ";
	private static final String LOG_CHOICE_ABORT = "User choice: ABORT";
	private static final String LOG_CHOICE_ERASE = "User choice: ERASE";
	private static final String LOG_CHOICE_OVERWRITE = "User choice: OVERWRITE";
	private static final String LOG_PREFIX_EXIST = "Prefix already exists";
	private static final String LOG_WINE_WAIT_FAILED = "Unable to wait for wine processes";

	private static final String DEFAULT_ARCHITECTURE = Architecture.I386.name();
	private static final String DEFAULT_DISTRIBUTION = "staging";
	private static final String OVERWRITE = "Overwrite (usually works, no guarantee)";
	private static final String ERASE = "Erase (virtual drive content will be lost)";
	private static final String ABORT = "Abort installation";

	private static final String REGEDIT = "regedit";
	private static final String POL = "pol";
	private static final String REGISTRY = "registry";
	private static final String STAR = "*";
	private static final String DLLOVERRIDES = "DllOverrides";
	private static final String WINE = "Wine";
	private static final String SOFTWARE = "Software";
	private static final String HKEY_CURRENT_USER = "HKEY_CURRENT_USER";
	
	private static final long NEWPREFIXSIZE = 320_000_000L;

	@Inject
	static PlayOnLinuxContext playOnLinuxContext;

	@Inject
	static ServiceManager backgroundServicesManager;

	@Inject
	static ExecutorService executorService;
	private final WineWizard setupWizard;

	private WinePrefix prefix;
	private String prefixName;
	private WineVersion wineVersion;
	private int lastReturnCode = -1;

	private OutputStream outputStream = new NullOutputStream();
	private OutputStream errorStream = new NullOutputStream();
	private InputStream inputStream = new NullInputStream(0);

	private Wine(WineWizard setupWizard) {
		this.setupWizard = setupWizard;
	}

	public static Wine wizard(WineWizard setupWizard) {
		final SetupWizardComponent wineInstance = new Wine(setupWizard);
		setupWizard.registerComponent(wineInstance);
		return new Wine(setupWizard);
	}

	/**
	 * Specifies the input stream
	 *
	 * @param inputStream
	 *            the input stream
	 * @return the same object
	 */
	public Wine withInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	/**
	 * Specifies the output stream
	 *
	 * @param outputStream
	 *            the output stream
	 * @return the same object
	 */
	public Wine withOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
		return this;
	}

	/**
	 * Specifies the error stream
	 *
	 * @param errorStream
	 *            the error stream
	 * @return the same object
	 */
	public Wine withErrorStream(OutputStream errorStream) {
		this.errorStream = errorStream;
		return this;
	}

	/**
	 * Select the prefix. If the prefix already exists, this method load its
	 * parameters and the prefix will be set as initialized.
	 *
	 * @param prefixName
	 *            the name of the prefix
	 * @return the same object
	 */
	public Wine selectPrefix(String prefixName) throws CancelException {
		this.prefixName = prefixName;
		this.prefix = new WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));

		if (prefix.initialized()) {
			wineVersion = new WineVersion(prefix.fetchVersion(), prefix.fetchDistribution(), setupWizard);
			if (!wineVersion.isInstalled()) {
				wineVersion.install();
			}
		}

		return this;
	}

	public Wine createPrefix(String version) throws CancelException {
		return this.createPrefix(version, DEFAULT_DISTRIBUTION);
	}

	/**
	 * Create the prefix and load its parameters. The prefix will be set as
	 * initialized
	 *
	 * @param version
	 *            version of wine
	 * @return the same object
	 * @throws CancelException
	 *             if the prefix cannot be created or if the user cancels the
	 *             operation
	 */
	public Wine createPrefix(String version, String distribution) throws CancelException {
		return this.createPrefix(version, distribution, DEFAULT_ARCHITECTURE);
	}

	/**
	 * Create the prefix and load its parameters. The prefix will be set as
	 * initialized
	 *
	 * @param version
	 *            version of wine
	 * @param architecture
	 *            architecture of wine
	 * @return the same object
	 * @throws CancelException
	 *             if the prefix cannot be created or if the user cancels the
	 *             operation
	 */
	public Wine createPrefix(String version, String distribution, String architecture) throws CancelException {
		if (prefix == null) {
			throw new ScriptFailureException(EXCEPTION_PREFIX_NOT_SELECTED);
		}

		if (prefix.exists() && userWantsToOverWritePrefix()) {
			return this;
		}

		wineVersion = new WineVersion(new Version(version),
				new WineDistribution(OperatingSystem.fetchCurrentOperationSystem(), Architecture.valueOf(architecture),
						distribution),
				setupWizard);

		if (!wineVersion.isInstalled()) {
			wineVersion.install();
		}

		final ProgressControl progressControl = this.setupWizard.progressBar(I18N_VIRTUAL_DRIVE_CREATION);

		try (DirectoryWatcherSize observableDirectorySize = new DirectoryWatcherSize(executorService,
				prefix.getWinePrefixDirectory().toPath())) {

			observableDirectorySize
					.setOnChange(newSize -> progressControl.setProgressPercentage(newSize * 100 / NEWPREFIXSIZE));
			Process process = wineVersion.getInstallation().createPrefix(this.prefix);
			waitWineProcess(process);
		} catch (WineException e) {
			throw new ScriptFailureException(e);
		}

		return this;
	}

	private void waitWineProcess(Process process) throws CancelException {
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			process.destroy();
			killall();
			throw new CancelException(e);
		}
	}

	private boolean userWantsToOverWritePrefix() throws CancelException {
		try {
			log(LOG_PREFIX_EXIST);

			// TODO use a more adapted data structure (like enums)
			switch (setupWizard.menu(format(I18N_VIRTUAL_DRIVE_EXIST, prefixName),
					Arrays.asList(OVERWRITE, ERASE, ABORT))) {
			case OVERWRITE:
				log(LOG_CHOICE_OVERWRITE);
				return true;
			case ERASE:
				log(LOG_CHOICE_ERASE);
				prefix.delete();
				return false;
			case ABORT:
			default:
				log(LOG_CHOICE_ABORT);
				throw new CancelException(EXCEPTION_SCRIPT_ABORTED);
			}
		} catch (IOException e) {
			throw new ScriptFailureException(e);
		}
	}

	/**
	 * Killall the processes in the prefix
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine killall() throws ScriptFailureException {
		validateWineInstallationInitialized();
		try {
			wineVersion.getInstallation().killAllProcess(this.prefix);
		} catch (IOException logged) {
			LOGGER.warn("Unable to kill wine processes", logged);
		}

		return this;
	}

	/**
	 * Run wine in the prefix
	 *
	 * @return the process object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	private Process runAndGetProcess(File workingDirectory, String executableToRun, List<String> arguments,
			Map<String, String> environment) throws ScriptFailureException {
		validateWineInstallationInitialized();
		validateArchitecture(workingDirectory, executableToRun);

		if (REGEDIT.equalsIgnoreCase(executableToRun)) {
			logRegFile(workingDirectory, arguments);
		}

		try {
			final Process process = wineVersion.getInstallation().run(prefix, workingDirectory, executableToRun,
					environment, arguments);

			if (this.setupWizard.getLogContext() != null) {
				final Service processPipe = new ProcessPipe(process,
						new TeeOutputStream(this.setupWizard.getLogContext(), outputStream),
						new TeeOutputStream(this.setupWizard.getLogContext(), errorStream), inputStream);
				backgroundServicesManager.register(processPipe);
			}
			return process;
		} catch (WineException e) {
			throw new ScriptFailureException(EXCEPTION_WINE_ERROR, e);
		}
	}

	private void logRegFile(File workingDirectory, List<String> pathToRegFile) throws ScriptFailureException {
		if (!pathToRegFile.isEmpty()) {
			final File regFile = findFile(workingDirectory, pathToRegFile.get(0));
			if (regFile.exists() && regFile.isFile()) {
				log(LOG_CONTENT_OF + regFile);
				log(LOG_LINE_SEPARATOR);
				try {
					log(FileUtils.readFileToString(regFile));
				} catch (IOException e) {
					LOGGER.warn(e);
				}
				log(LOG_LINE_SEPARATOR);
			} else {
				log(regFile + LOG_NOT_A_FILE);
			}
		} else {
			log(LOG_MANUALLY_MODIFIED_REGISTRY);
		}
	}

	private void log(String message) throws ScriptFailureException {
		setupWizard.log(message);

		if (prefix != null) {
			try {
				prefix.log(message);
			} catch (IOException e) {
				setupWizard.log(LOG_LOGGING_WINEPREFIX_FAILED, e);
			}
		}
	}

	private void validateArchitecture(File workingDirectory, String executableToRun) {
		try {
			if (wineVersion.getWineDistribution().getArchitecture() == Architecture.I386) {
				final File executedFile = findFile(workingDirectory, executableToRun);
				if (executedFile.exists() && ExeAnalyser.is64Bits(executedFile)) {
					throw new IllegalStateException(EXCEPTION_PREFIX_INCOMPATIBLE_ARCH);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private File findFile(File workingDirectory, String executableToRun) {
		if (new File(workingDirectory, executableToRun).exists()) {
			return new File(workingDirectory, executableToRun);
		} else {
			return new File(executableToRun);
		}
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(File workingDirectory, String executableToRun, List<String> arguments,
			Map<String, String> environment) throws ScriptFailureException {

		runAndGetProcess(workingDirectory, executableToRun, arguments, environment);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(File executableToRun, List<String> arguments, Map<String, String> environment)
			throws ScriptFailureException {
		File workingDirectory = executableToRun.getParentFile();
		runBackground(workingDirectory, executableToRun.getAbsolutePath(), arguments, environment);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(String executableToRun, List<String> arguments, Map<String, String> environment)
			throws ScriptFailureException {
		runBackground(this.prefix.getWinePrefixDirectory(), executableToRun, arguments, environment);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(String executableToRun, List<String> arguments) throws ScriptFailureException {
		runBackground(new File(executableToRun), arguments, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(File executableToRun, List<String> arguments) throws ScriptFailureException {
		runBackground(executableToRun, arguments, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @param executableToRun
	 *            executable to run (file parameter)
	 * @return the same object
	 * @throws ScriptFailureException
	 */
	public Wine runBackground(File executableToRun) throws ScriptFailureException {
		runBackground(executableToRun, (List<String>) null, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @param executableToRun
	 *            executable to run (string parameter)
	 * @return the same object
	 * @throws ScriptFailureException
	 */
	public Wine runBackground(String executableToRun) throws ScriptFailureException {
		runBackground(executableToRun, null, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(File workingDirectory, String executableToRun, List<String> arguments)
			throws ScriptFailureException {
		runBackground(workingDirectory, executableToRun, arguments, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runBackground(File workingDirectory, String executableToRun) throws ScriptFailureException {
		runBackground(workingDirectory, executableToRun, null, null);
		return this;
	}

	/**
	 * Run wine in the prefix in foreground
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
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
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(String workingDirectory, String executableToRun, List<String> arguments,
			Map<String, String> environment) throws CancelException {
		return runForeground(new File(workingDirectory), executableToRun, arguments, environment);
	}

	/**
	 * Run wine in the prefix in foreground
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(File executableToRun, List<String> arguments, Map<String, String> environment)
			throws CancelException {
		File workingDirectory = executableToRun.getParentFile();
		runForeground(workingDirectory, executableToRun.getAbsolutePath(), arguments, environment);
		return this;
	}

	/**
	 * Run wine in the prefix in foreground
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(String executableToRun, List<String> arguments, Map<String, String> environment)
			throws CancelException {
		runForeground(this.prefix.getWinePrefixDirectory(), executableToRun, arguments, environment);
		return this;
	}

	/**
	 * Run wine in the prefix in foreground
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(String executableToRun, List<String> arguments) throws CancelException {
		runForeground(new File(executableToRun), arguments, null);
		return this;
	}

	/**
	 * Run wine in the prefix in foreground
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(File executableToRun, List<String> arguments) throws CancelException {
		runForeground(executableToRun, arguments, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @param executableToRun
	 *            executable to run (file parameter)
	 * @return the same object
	 * @throws ScriptFailureException
	 */
	public Wine runForeground(File executableToRun) throws CancelException {
		runForeground(executableToRun, (List<String>) null, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @param executableToRun
	 *            executable to run (string parameter)
	 * @return the same object
	 * @throws ScriptFailureException
	 */
	public Wine runForeground(String executableToRun) throws CancelException {
		runForeground(executableToRun, null, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(File workingDirectory, String executableToRun, List<String> arguments)
			throws CancelException {
		runForeground(workingDirectory, executableToRun, arguments, null);
		return this;
	}

	/**
	 * Run wine in the prefix in background
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine runForeground(File workingDirectory, String executableToRun) throws CancelException {
		runForeground(workingDirectory, executableToRun, null, null);
		return this;
	}

	/**
	 * Wait for all wine application to be terminated
	 *
	 * @return the same object
	 * @throws ScriptFailureException
	 *             if the wine prefix is not initialized
	 */
	public Wine waitExit() throws ScriptFailureException {
		validateWineInstallationInitialized();
		try {
			wineVersion.getInstallation().waitAllProcesses(this.prefix);
		} catch (IOException logged) {
			LOGGER.warn(LOG_WINE_WAIT_FAILED, logged);
		}

		return this;
	}

	/**
	 * Wait for all wine application to be terminated and createPrefix a
	 * progress bar watching for the size of a directory
	 *
	 * @param directory
	 *            Directory to watch
	 * @param endSize
	 *            Expected size of the directory when the installation is
	 *            terminated
	 * @return the same object
	 * @throws CancelException
	 *             if the users cancels or if there is any error
	 */
	public Wine waitAllWatchDirectory(File directory, long endSize) throws CancelException {
		ProgressControl progressControl = this.setupWizard.progressBar(I18N_PROGRAM_INSTALLING);

		final long startSize = FileUtils.sizeOfDirectory(directory);

		try (DirectoryWatcherSize observableDirectorySize = new DirectoryWatcherSize(executorService,
				prefix.getWinePrefixDirectory().toPath())) {

			observableDirectorySize.setOnChange(newSize -> {
				final double percentage = 100. * (newSize - startSize) / (endSize - startSize);
				progressControl.setProgressPercentage(percentage);
			});

			waitExit();
		} catch (IllegalStateException e) {
			throw new ScriptFailureException(e);
		}

		return this;
	}

	/**
	 * Delete the wineprefix
	 *
	 * @return the same object
	 * @throws CancelException
	 *             if the users cancels or if there is any error
	 */
	public Wine deletePrefix() throws CancelException {
		if (prefix.getWinePrefixDirectory().exists()) {
			ProgressControl progressControl = this.setupWizard.progressBar(I18N_VIRTUAL_DRIVE_DELETION);
			final long startSize = prefix.getSize();
			final long endSize = 0L;

			try (DirectoryWatcherSize observableDirectorySize = new DirectoryWatcherSize(executorService,
					prefix.getWinePrefixDirectory().toPath())) {

				observableDirectorySize.setOnChange(newSize -> {
					final double percentage = 100. * (newSize - startSize) / (endSize - startSize);
					progressControl.setProgressPercentage(percentage);
				});

				prefix.delete();
			} catch (IOException | IllegalStateException e) {
				throw new ScriptFailureException(e);

			}
		}

		return this;
	}

	/**
	 * Overides DLLs parameters
	 *
	 * @param dllsToOverride
	 *            a Map containing the dlls to overide (key = name of the dll,
	 *            value = [disabled, builtin, native])
	 * @return the same object
	 */
	public Wine overrideDlls(Map<String, String> dllsToOverride) throws ScriptFailureException {
		validateWineInstallationInitialized();
		final RegistryKey hkeyCurrentUser = new RegistryKey(HKEY_CURRENT_USER);
		final RegistryKey software = new RegistryKey(SOFTWARE);
		final RegistryKey wine = new RegistryKey(WINE);
		final RegistryKey dllOverrides = new RegistryKey(DLLOVERRIDES);

		hkeyCurrentUser.addChild(software);
		software.addChild(wine);
		wine.addChild(dllOverrides);

		for (String dll : dllsToOverride.keySet()) {
			final RegistryValue<StringValueType> dllNode = new RegistryValue<>(STAR + dll,
					new StringValueType(dllsToOverride.get(dll)));
			dllOverrides.addChild(dllNode);
		}

		writeToRegistry(hkeyCurrentUser);

		return this;
	}

	private void validateWineInstallationInitialized() throws ScriptFailureException {
		if (wineVersion == null) {
			throw new ScriptFailureException(EXCEPTION_PREFIX_NOT_INITIALIZED);
		}
	}

	private void writeToRegistry(AbstractRegistryNode node) throws ScriptFailureException {
		final RegistryWriter registryWriter = new RegistryWriter();
		final File temporaryRegFile;
		try {
			temporaryRegFile = File.createTempFile(REGISTRY, POL);
			temporaryRegFile.deleteOnExit();
			com.google.common.io.Files.write(registryWriter.generateRegFileContent(node).getBytes(), temporaryRegFile);
		} catch (IOException e) {
			throw new ScriptFailureException(e);
		}

		final List<String> arguments = new ArrayList<>();
		arguments.add(temporaryRegFile.getAbsolutePath());

		this.runAndGetProcess(prefix.getDriveCPath(), REGEDIT, arguments, new HashMap<>());
	}

	public ConfigFile config() {
		return prefix.getPrefixConfigFile();
	}

	public int getLastReturnCode() {
		return lastReturnCode;
	}

	@Override
	public void close() {
		if (prefix != null) {
			prefix.close();
		}
	}

	public Collection<File> findExecutables() {
		return prefix.findAllExecutables();
	}
}
