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

import com.playonlinux.common.api.ui.ProgressStep;
import com.playonlinux.domain.CancelException;
import com.playonlinux.domain.ScriptClass;
import com.playonlinux.domain.ScriptFailureException;
import com.playonlinux.utils.Checksum;
import com.playonlinux.domain.PlayOnLinuxException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import static com.playonlinux.domain.Localisation.translate;

@ScriptClass
@SuppressWarnings("unused")
public class Downloader {
    String MD5_CHECKSUM = "md5";

    private SetupWizard setupWizard;
    private ProgressStep progressStep;

    private static final int BLOCK_SIZE = 1024;
    private File downloadedFile;

    /**
     * Create a downloader object that is not hook into any progress bar
     */
    public Downloader() {

    }

    public Downloader(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    public Downloader(ProgressStep progressStep) {
        this.progressStep = progressStep;
    }

    private void defineProgressStep(URL remoteFile) throws CancelException, InterruptedException {
        if(this.progressStep == null) {
            this.progressStep = this.setupWizard.progressBar(
                    translate("Please wait while ${application.name} is downloading:") + "\n" +
                    this.findFileNameFromURL(remoteFile)
            );
        }
    }
    private HttpURLConnection openConnection(URL remoteFile) throws IOException {
        return (HttpURLConnection) remoteFile.openConnection();
    }

    private void saveConnectionToFile(HttpURLConnection connection, File localFile) throws IOException, CancelException {
        int fileSize = connection.getContentLength();
        float totalDataRead = 0;

        BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile), BLOCK_SIZE);

        byte[] data = new byte[BLOCK_SIZE];
        int i;
        while((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0)
        {
            totalDataRead += i;
            outputStream.write(data, 0, i);
            if(progressStep != null) {
                int percentDownloaded = (int) ((totalDataRead * 100) / fileSize);
                progressStep.setProgressPercentage(percentDownloaded);
            }
            if(Thread.currentThread().isInterrupted()) {
                throw new CancelException("The download has been aborted");
            }
        }
        inputStream.close();
        outputStream.close();
    }

    public Downloader get(URL remoteFile, File localFile) throws IOException, CancelException, InterruptedException {
        this.defineProgressStep(remoteFile);
        HttpURLConnection connection = openConnection(remoteFile);
        this.saveConnectionToFile(connection, localFile);
        this.downloadedFile = localFile;
        return this;
    }

    public Downloader get(URL remoteFile) throws CancelException {
        File temporaryFile;
        try {
            temporaryFile = File.createTempFile(this.findFileNameFromURL(remoteFile), "");
        } catch (IOException e) {
            throw new ScriptFailureException("Unable to create temporary log file", e);
        }
        try {
            return get(remoteFile, temporaryFile);
        } catch (IOException e) {
            throw new ScriptFailureException("Unable to download the remote file", e);
        } catch (InterruptedException e) {
            throw new CancelException(e);
        }
    }

    public Downloader get(String remoteFile) throws CancelException {
        try {
            return get(new URL(remoteFile));
        } catch (MalformedURLException e) {
            throw new ScriptFailureException(String.format("Unable to download the remote file: %s", remoteFile), e);
        }
    }

    public Downloader get(String remoteFile, String localFile) throws CancelException {
        try {
            return get(new URL(remoteFile), new File(localFile));
        } catch (IOException  e) {
            throw new ScriptFailureException("The download has failed", e);
        } catch (InterruptedException e) {
            throw new CancelException(e);
        }
    }

    public Downloader check(String expectedChecksum) throws IOException, NoSuchAlgorithmException, PlayOnLinuxException {
        String calculatedChecksum = Checksum.calculate(this.findDownloadedFile(), MD5_CHECKSUM);
        if(this.findDownloadedFile() == null) {
            throw new PlayOnLinuxException("You must download the file first before running check()!");
        }
        if(!expectedChecksum.equals(calculatedChecksum)) {
            throw new PlayOnLinuxException(String.format("Checksum comparison has failed!%n%nServer: %s%nClient: %s",
                    expectedChecksum, calculatedChecksum));
        }

        return this;
    }

    protected String findFileNameFromURL(URL remoteFile) {
        String[] urlParts = remoteFile.getFile().split("/");
        return urlParts[urlParts.length - 1];
    }

    public File findDownloadedFile() {
        return downloadedFile;
    }


}
