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

import static com.playonlinux.core.lang.Localisation.translate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.core.scripts.ScriptClass;
import com.playonlinux.core.scripts.ScriptFailureException;
import com.playonlinux.framework.wizard.SetupWizardComponent;
import com.playonlinux.ui.api.ProgressControl;

/* A builder pattern could be used here but we chose not to use it to facilitate com.playonlinux.core.scripts.sh syntax
 */

@ScriptClass
@SuppressWarnings("unused")
public class Files implements SetupWizardComponent {
    private SetupWizard setupWizard;
    private ProgressControl progressControl;

    private static final int BLOCK_SIZE = 1024;

    /**
     * Create a downloader object that is not hooked to any progress bar
     */
    public Files() {

    }

    public Files(ProgressControl progressControl) {
	this.progressControl = progressControl;
    }

    private Files(SetupWizard setupWizard) {
	this.setupWizard = setupWizard;
    }

    public static Files wizard(SetupWizard setupWizard) {
	final SetupWizardComponent filesInstance = new Files(setupWizard);
	setupWizard.registerComponent(filesInstance);
	return new Files(setupWizard);
    }

    private void defineProgressStep(File sourceFile) throws CancelException {
	if (this.progressControl == null) {
	    this.progressControl = this.setupWizard.progressBar(
		    translate("Please wait while ${application.name} is copying:") + "\n" + sourceFile.getName());
	}
    }

    private void copyFile(File sourceFile, File destinationFile) throws IOException, CancelException {
	int fileSize = (int) sourceFile.length();
	float totalDataRead = 0.0F;

	try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile),
			BLOCK_SIZE)) {
	    byte[] data = new byte[BLOCK_SIZE];
	    int i;
	    while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
		totalDataRead += i;
		outputStream.write(data, 0, i);
		if (progressControl != null) {
		    int percentCopied = (int) (totalDataRead * 100 / fileSize);
		    progressControl.setProgressPercentage(percentCopied);
		}

		if (Thread.interrupted()) {
		    throw new CancelException("The copy process was interrupted");
		}
	    }
	    inputStream.close();
	    outputStream.close();
	}
    }

    public Files copy(String sourceFilePath, String destinationFilePath) throws CancelException {
	File sourceFile = new File(sourceFilePath);
	File destinationFile = new File(destinationFilePath);

	this.defineProgressStep(sourceFile);

	try {
	    this.copyFile(sourceFile, destinationFile);
	} catch (IOException e) {
	    throw new ScriptFailureException(e);
	}
	return this;
    }

    public Files mkdir(String directoryToCreate) throws ScriptFailureException {
	try {
	    java.nio.file.Files.createDirectories(new File(directoryToCreate).toPath());
	} catch (IOException e) {
	    throw new ScriptFailureException(
		    String.format("Unable to createPrefix the directory %s", directoryToCreate), e);
	}

	return this;
    }

    public Files remove(String pathToDelete) throws ScriptFailureException {
	try {
	    com.playonlinux.core.utils.Files.remove(new File(pathToDelete));
	} catch (IOException e) {
	    throw new ScriptFailureException("Unable to deletePrefix the file", e);
	}

	return this;
    }

    @Override
    public void close() {
	// Nothing to do for the moment
    }
}
