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

package com.playonlinux.containers;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.framework.Wine;
import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.wine.WineConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Scan
public class WineContainerActions {
    @Inject
    static ExecutorService executorService;

    public void runWinecfg(WineWizard containerSetupWizard, File winePrefixDirectory, Consumer<Void> callBack) {
        executorService.submit(() -> {
            log.info("Will run winecfg in " + winePrefixDirectory.getPath());
            containerSetupWizard.init();
            try {
                Wine.wizard(containerSetupWizard).selectPrefix(winePrefixDirectory.getName())
                        .runForeground(WineConstants.WINECFG);
            } catch (CancelException e) {
                log.info("Failed to run wine cfg", e);
            }
            callBack.accept(null);
            containerSetupWizard.close();
        });

    }
}
