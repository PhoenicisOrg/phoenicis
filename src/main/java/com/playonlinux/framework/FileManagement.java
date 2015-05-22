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

import com.playonlinux.api.ui.ProgressStep;
import com.playonlinux.domain.CancelException;
import com.playonlinux.domain.ScriptClass;

import java.io.*;

import static com.playonlinux.domain.Localisation.translate;

/* A builder pattern could be used here but we chose not to use it to facilitate com.playonlinux.scripts syntax
 */

// TODO: Create an abstract class for FileManagement and Downloader
@ScriptClass
@SuppressWarnings("unused")
public class FileManagement {
    private SetupWizard setupWizard;
    private ProgressStep progressStep;

    private static final int BLOCK_SIZE = 1024;

    /**
     * Create a downloader object that is not hook into any progress bar
     */
    public FileManagement() {

    }
    public FileManagement(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    public FileManagement(ProgressStep progressStep) {
        this.progressStep = progressStep;
    }

    private void defineProgressStep(File sourceFile) throws CancelException, InterruptedException {
        if(this.progressStep == null) {
            this.progressStep = this.setupWizard.progressBar(
                    translate("Please wait while ${application.name} is copying:") + "\n" +
                            sourceFile.getName()
            );
        }
    }

    private void copyFile(File sourceFile, File destinationFile) throws IOException {
        int fileSize = (int) sourceFile.length();
        float totalDataRead = 0;

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile), BLOCK_SIZE);

        byte[] data = new byte[BLOCK_SIZE];
        int i;
        while((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0)
        {
            totalDataRead += i;
            outputStream.write(data, 0, i);
            if(progressStep != null) {
                int percentCopied = (int) ((totalDataRead * 100) / fileSize);
                progressStep.setProgressPercentage(percentCopied);
            }
        }
        inputStream.close();
        outputStream.close();
    }

    public FileManagement copy(String sourceFilePath, String destinationFilePath)
            throws CancelException, InterruptedException, IOException {
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        this.defineProgressStep(sourceFile);

        this.copyFile(sourceFile, destinationFile);
        return this;
    }


}
