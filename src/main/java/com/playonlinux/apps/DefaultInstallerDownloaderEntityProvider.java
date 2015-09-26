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

package com.playonlinux.apps;

import com.playonlinux.apps.entities.InstallerDownloaderEntity;
import com.playonlinux.core.entities.ProgressStateEntity;
import com.playonlinux.core.gpg.SignatureChecker;
import com.playonlinux.core.gpg.SignatureException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.scripts.Script;
import com.playonlinux.core.scripts.ScriptFactory;
import com.playonlinux.core.scripts.ScriptFailureException;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.webservice.DownloadManager;
import com.playonlinux.core.webservice.HTTPDownloader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

@Scan
public class DefaultInstallerDownloaderEntityProvider
        extends ObservableDefaultImplementation<InstallerDownloaderEntity>
        implements InstallerDownloaderEntityProvider,
                   Observer<HTTPDownloader, ProgressStateEntity> {

    @Inject
    static ScriptFactory scriptFactory;

    @Inject
    static ServiceManager serviceManager;

    private final DownloadManager downloadManager = serviceManager.getService(DownloadManager.class);

    private static final Logger LOGGER = Logger.getLogger(DefaultInstallerDownloaderEntityProvider.class);
    private final HTTPDownloader httpDownloader;
    private final File localFile;
    private final SignatureChecker signatureChecker;

    DefaultInstallerDownloaderEntityProvider(HTTPDownloader httpDownloader, SignatureChecker signatureChecker) {
        this.httpDownloader = httpDownloader;
        this.signatureChecker = signatureChecker;

        try {
            this.localFile = File.createTempFile("script", "pol");
            this.localFile.deleteOnExit();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void getScript() {
        httpDownloader.addObserver(this);

        downloadManager.submit(httpDownloader, bytes -> {
            success(bytes);
            return null;
        }, e -> {
            failure(e);
            return null;
        });

    }

    private void success(byte[] bytes) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(localFile)) {
            fileOutputStream.write(bytes);
            terminateDownload();
        } catch (IOException e) {
            LOGGER.error(e);
            failure(e);
        }
    }

    private void failure(Exception e) {
        LOGGER.warn(e);
        this.changeState(State.FAILED);
    }

    private void changeState(State state) {
        changeState(state, 0.);
    }

    private void changeState(State state, double percentage) {
        changeState(state, percentage, null);
    }

    private void changeState(State state, double percentage, String scriptContent) {
        boolean finished = state == State.SUCCESS || state == State.FAILED;
        boolean failed = state == State.FAILED;
        boolean signatureError = state == State.SIGNATURE_ERROR;

        notifyObservers(new InstallerDownloaderEntity(finished, failed, signatureError, percentage, scriptContent));
    }

    public enum State {
        READY,
        PROGRESSING,
        SUCCESS,
        FAILED,
        SIGNATURE_ERROR
    }

    @Override
    public void update(HTTPDownloader observable, ProgressStateEntity argument) {
        if(argument.getState() == ProgressStateEntity.State.PROGRESSING) {
            changeState(State.PROGRESSING, argument.getPercent());
        }
    }

    private void terminateDownload() {
        try {
            final Script script = scriptFactory.createInstance(localFile);
            final String scriptContent = script.extractContent();

            try {
                this.signatureChecker
                        .withSignature(script.extractSignature())
                        .withData(scriptContent)
                        .withPublicKey(SignatureChecker.getPublicKey());

                if (!signatureChecker.check()) {
                    changeState(State.SIGNATURE_ERROR, 100., scriptContent);
                } else {
                    changeState(State.SUCCESS, 100.);
                    startScript(script);
                }
            } catch (SignatureException e) {
                LOGGER.error(e);
                changeState(State.SIGNATURE_ERROR, 100, scriptContent);
            } catch (ServiceInitializationException e) {
                LOGGER.info(e);
            }
        } catch (ScriptFailureException e) {
            LOGGER.error(e);
            changeState(State.FAILED);
        }
    }

    private void startScript(Script script) throws ServiceInitializationException {
        serviceManager.register(script);
    }


}
