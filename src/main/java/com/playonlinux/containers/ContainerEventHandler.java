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

import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.framework.NullSetupWizard;
import com.playonlinux.framework.Wine;
import org.apache.log4j.Logger;

import java.io.File;

public class ContainerEventHandler {
    private static final Logger LOGGER = Logger.getLogger(ContainerEventHandler.class);

    public void runWinecfg(File winePrefixDirectory) {
        Wine wine = null;
        try {
            wine = Wine.wizard(new NullSetupWizard())
                    .selectPrefix(winePrefixDirectory.getName())
                    .runBackground("winecfg");
        } catch (CancelException e) {
            LOGGER.info(e);
        }

        wine.close();
    }
}
