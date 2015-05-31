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
import com.playonlinux.common.api.services.BackgroundServiceManager;
import com.playonlinux.common.api.ui.ProgressStep;
import com.playonlinux.domain.CancelException;
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.domain.ScriptClass;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.ObservableDirectorySize;
import com.playonlinux.wine.WineInstallation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.playonlinux.domain.Localisation.translate;

@Scan
@ScriptClass
@SuppressWarnings("unused")
public class WinePrefix {
    Logger logger = Logger.getLogger(WinePrefix.class);

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static BackgroundServiceManager backgroundServicesManager;

    private final static long NEWPREFIXSIZE = 320_000_000;

    private final SetupWizard setupWizard;
    private com.playonlinux.wine.WinePrefix prefix;
    private String prefixName;
    private WineInstallation wineInstallation;

    public WinePrefix(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    public WinePrefix select(String prefixName) {
        this.prefixName = prefixName;
        this.prefix = new com.playonlinux.wine.WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));
        return this;
    }

    public WinePrefix create(String version) throws ScriptFailureException {
        try {
            return this.create(version, Architecture.fetchCurrentArchitecture().name());
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException("Unable to create the wineprefix", e);
        }
    }

    public WinePrefix create(String version, String architecture) throws CancelException {
        if(prefix == null) {
            throw new ScriptFailureException("Prefix must be selected!");
        }

        try {
            wineInstallation = new WineInstallation.Builder()
                    .withPath(playOnLinuxContext.makeWinePathFromVersionAndArchitecture(
                            version,
                            Architecture.valueOf(architecture))
                    ).withApplicationEnvironment(playOnLinuxContext.getSystemEnvironment())
                    .build();
        } catch (PlayOnLinuxException e) {
            throw new ScriptFailureException(e);
        }

        /* Maybe it needs to be better implemented */
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
        } catch (IOException e) {
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

    public WinePrefix killall() {
        try {
            wineInstallation.killAllProcess(this.prefix);
        } catch (IOException logged) {
            logger.warn("Unable to kill wine processes", logged);
        }

        return this;
    }

    public WinePrefix waitEnd() {
        try {
            wineInstallation.waitAllProcesses(this.prefix);
        } catch (IOException logged) {
            logger.warn("Unable to wait for wine processes", logged);
        }

        return this;
    }

    public WinePrefix waitEndDirectoryProgress(File directory, long endSize) throws CancelException {
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
            waitEnd();
        } finally {
            observableDirectorySize.deleteObserver(progressStep);
            backgroundServicesManager.unregister(observableDirectorySize);
        }


        return this;
    }

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
}
