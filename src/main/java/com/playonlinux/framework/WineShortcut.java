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
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.scripts.ScriptClass;
import com.playonlinux.framework.wizard.SetupWizardComponent;
import com.playonlinux.library.LibraryException;
import com.playonlinux.library.ShortcutCreator;

import java.io.File;
import java.util.List;

import static java.lang.String.format;

@Scan
@ScriptClass
@SuppressWarnings("unused")
public class WineShortcut implements SetupWizardComponent {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;
    private final SetupWizard setupWizard;
    private final com.playonlinux.library.shortcuts.WineShortcut.Builder wineShortcutBuilder;
    private String name;

    private WineShortcut(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
        this.wineShortcutBuilder = new com.playonlinux.library.shortcuts.WineShortcut.Builder();
    }

    public static WineShortcut wizard(SetupWizard setupWizard) {
        WineShortcut wineInstance = new WineShortcut(setupWizard);
        setupWizard.registerComponent(wineInstance);
        return new WineShortcut(setupWizard);
    }

    public WineShortcut withWineDebug(String wineDebug) {
        wineShortcutBuilder.withWineDebug(wineDebug);
        return this;
    }

    public WineShortcut withArguments(List<String> arguments) {
        wineShortcutBuilder.withArguments(arguments);
        return this;
    }

    public WineShortcut withExecutableName(String executableName) {
        wineShortcutBuilder.withExecutableName(executableName);
        return this;
    }

    public WineShortcut withWorkingDirectory(String workingDirectory) {
        wineShortcutBuilder.withWorkingDirectory(workingDirectory);
        return this;
    }

    public WineShortcut withWinePrefix(String winePrefix) {
        wineShortcutBuilder.withWinePrefix(winePrefix);
        return this;
    }

    public WineShortcut withName(String name) {
        this.name = name;
        return this;
    }

    public void create() throws ScriptFailureException {
        final ShortcutCreator shortcutCreator = new ShortcutCreator();
        if(name == null) {
            throw new ScriptFailureException("You must provide a valid name for your shortcut. Aborting");
        }
        try {
            shortcutCreator.createShortcut(
                    new File(playOnLinuxContext.makeShortcutsPath(), name),
                    wineShortcutBuilder.create()
            );
        } catch (LibraryException e) {
            throw new ScriptFailureException(e);
        }
    }


    @Override
    public void close() {
        // Nothing to close
    }
}
