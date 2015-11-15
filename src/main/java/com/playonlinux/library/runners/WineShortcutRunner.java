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

package com.playonlinux.library.runners;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.framework.Wine;
import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.library.shortcuts.WineShortcut;
import com.playonlinux.wine.WineConstants;

public class WineShortcutRunner {
    private final WineShortcut wineShortcut;

    public WineShortcutRunner(WineShortcut wineShortcut) {
        this.wineShortcut = wineShortcut;
    }

    public void run(WineWizard wineWizard) throws CancelException {
        final Map<String, String> wineDebugMap = new HashMap<>();
        wineDebugMap.put(WineConstants.WINEDEBUG, wineShortcut.getWineDebug());
        Wine.wizard(wineWizard).selectPrefix(wineShortcut.getWinePrefix())
                .runBackground(
                        new File(wineShortcut.getWorkingDirectory()),
                        wineShortcut.getExecutableName(),
                        wineShortcut.getArguments(),
                        wineDebugMap
                );
    }
}
