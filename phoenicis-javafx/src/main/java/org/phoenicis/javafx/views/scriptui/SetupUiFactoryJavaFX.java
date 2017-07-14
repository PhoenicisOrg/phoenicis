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

package org.phoenicis.javafx.views.scriptui;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.library.ViewLibrary;
import org.phoenicis.scripts.ui.SetupUi;
import org.phoenicis.scripts.ui.SetupUiFactory;
import org.phoenicis.tools.system.OperatingSystemFetcher;

@Safe
public class SetupUiFactoryJavaFX implements SetupUiFactory {

    private OperatingSystemFetcher operatingSystemFetcher;
    private ThemeManager themeManager;
    private ViewLibrary viewLibrary;

    public SetupUiFactoryJavaFX(OperatingSystemFetcher operatingSystemFetcher, ThemeManager themeManager,
            ViewLibrary viewLibrary) {
        super();
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.themeManager = themeManager;
        this.viewLibrary = viewLibrary;
    }

    @Override
    public SetupUi createSetupWindow(String title) {
        final SetupUiJavaFXImplementation setupWindow = new SetupUiJavaFXImplementation(title,
                this.operatingSystemFetcher, this.themeManager);
        this.viewLibrary.closeDetailsView();
        this.viewLibrary.createNewTab(setupWindow);
        setupWindow.setOnShouldClose(() -> this.viewLibrary.closeTab(setupWindow));
        return setupWindow;
    }
}
