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
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.domain.ScriptClass;
import com.playonlinux.injection.Scan;
import com.playonlinux.injection.Inject;
import com.playonlinux.utils.Architecture;
import com.playonlinux.domain.CancelException;
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.wine.WineInstallation;

import java.io.IOException;

import static com.playonlinux.domain.Localisation.translate;

@Scan
@ScriptClass
@SuppressWarnings("unused")
public class WinePrefix {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private final static long NEWPREFIXSIZE = 320_000_000;

    private final SetupWizard setupWizard;
    private com.playonlinux.wine.WinePrefix prefix;
    private String prefixName;

    public WinePrefix(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    public WinePrefix select(String prefixName) {
        this.prefixName = prefixName;
        this.prefix = new com.playonlinux.wine.WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));
        return this;
    }

    public WinePrefix create(String version) throws PlayOnLinuxException, IOException, InterruptedException, CancelException {
        return this.create(version, Architecture.fetchCurrentArchitecture().name());
    }

    public WinePrefix create(String version, String architecture) throws IOException, PlayOnLinuxException,
            InterruptedException, CancelException {
        if(prefix == null) {
            throw new PlayOnLinuxException("Prefix must be selected!");
        }
        WineInstallation wineInstallation = new WineInstallation.Builder()
                .withPath(playOnLinuxContext.makeWinePathFromVersionAndArchitecture(
                        version,
                        Architecture.valueOf(architecture))
                ).withApplicationEnvironment(playOnLinuxContext.getSystemEnvironment())
                .build();

        Process process = wineInstallation.createPrefix(this.prefix);

        /* Maybe it needs to be better implemented */
        ProgressStep progressStep =
                this.setupWizard.progressBar(String.format(translate("Please wait while the prefix %s is created"), prefixName));

        while(process.isAlive()) {
            double percentage = this.prefix.getSize() * 100. / (double) NEWPREFIXSIZE;
            progressStep.setProgressPercentage(percentage);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                process.destroy();
                wineInstallation.killAllProcess(this.prefix);
                throw new CancelException();
            }
        }
        process.waitFor();

        return this;
    }
}
