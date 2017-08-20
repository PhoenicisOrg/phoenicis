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

package org.phoenicis.javafx.controller.settings;

import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.mainwindow.settings.ViewSettings;

public class JavaFxSettingsController {
    private final ViewSettings view;
    private final JavaFxSettingsManager javaFxSettingsManager;

    public JavaFxSettingsController(ViewSettings view, JavaFxSettingsManager javaFxSettingsManager) {
        this.view = view;
        this.javaFxSettingsManager = javaFxSettingsManager;
    }

    public ViewSettings getView() {
        return view;
    }
}
