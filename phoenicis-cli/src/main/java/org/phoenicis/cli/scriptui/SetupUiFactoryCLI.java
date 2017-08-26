/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.cli.scriptui;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.scripts.ui.SetupUi;
import org.phoenicis.scripts.ui.SetupUiFactory;

import java.net.URI;
import java.util.Optional;

/**
 * CLI implementation of the SetupUiFactory
 */
@Safe
public class SetupUiFactoryCLI implements SetupUiFactory {

    /**
     * constructor
     */
    public SetupUiFactoryCLI() {
        super();
    }

    /**
     * creates a setup UI to install an application
     * @param title title of the setup UI
     * @param miniature miniature of the setup UI (usually the miniature of the installed application)
     * @return created setup UI
     */
    @Override
    public SetupUi createSetupWindow(String title, Optional<URI> miniature) {
        return new SetupUiCliImplementation(title, true, true);
    }
}
