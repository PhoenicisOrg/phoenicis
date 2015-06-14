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

import com.playonlinux.ui.ProgressStep;
import com.playonlinux.domain.CancelException;
import com.playonlinux.domain.ScriptClass;
import com.playonlinux.utils.Checksum;
import com.playonlinux.webservice.DownloadException;
import com.playonlinux.webservice.HTTPDownloader;

import java.io.File;
import java.io.IOException;
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

    private void defineProgressStep(URL remoteFile) throws CancelException {
        if(this.progressStep == null) {
            this.progressStep = this.setupWizard.progressBar(
                    translate("Please wait while ${application.name} is downloading:") + "\n" +
                    this.findFileNameFromURL(remoteFile)
            );
        }
    }



    private Downloader downloadRemoteFile(URL remoteFile, File localFile) throws CancelException {

        this.defineProgressStep(remoteFile);

        HTTPDownloader downloader = new HTTPDownloader(remoteFile);
        try {
            downloader.addObserver(progressStep);
            downloader.get(localFile);
        } catch (DownloadException e) {
            throw new ScriptFailureException("Unable to download the file", e);
        } finally {
            downloader.deleteObserver(progressStep);
        }

        downloadedFile = localFile;
        return this;
    }

    public Downloader get(URL remoteFile) throws CancelException {
        File temporaryFile;
        try {
            temporaryFile = File.createTempFile(this.findFileNameFromURL(remoteFile), "");
            temporaryFile.deleteOnExit();
        } catch (IOException e) {
            throw new ScriptFailureException("Unable to create temporary log file", e);
        }

        return downloadRemoteFile(remoteFile, temporaryFile);


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
            return downloadRemoteFile(new URL(remoteFile), new File(localFile));
        } catch (MalformedURLException e) {
            throw new ScriptFailureException(e);
        }
    }

    public Downloader check(String expectedChecksum) throws ScriptFailureException {
        String calculatedChecksum;
        try {
            calculatedChecksum = Checksum.calculate(this.findDownloadedFile(), MD5_CHECKSUM);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new ScriptFailureException(e);
        }
        if(this.findDownloadedFile() == null) {
            throw new ScriptFailureException("You must download the file first before running check()!");
        }
        if(!expectedChecksum.equals(calculatedChecksum)) {
            throw new ScriptFailureException(String.format("Checksum comparison has failed!%n%nServer: %s%nClient: %s",
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
