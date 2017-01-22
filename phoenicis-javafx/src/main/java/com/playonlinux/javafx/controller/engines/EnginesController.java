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

package com.playonlinux.javafx.controller.engines;

import com.playonlinux.engines.WineVersionsManager;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import javafx.application.Platform;

public class EnginesController {
    private final ViewEngines viewEngines;
    private final WineVersionsManager wineVersionsManager;

    public EnginesController(ViewEngines viewEngines, WineVersionsManager wineVersionsManager) {
        this.viewEngines = viewEngines;
        this.wineVersionsManager = wineVersionsManager;
    }

    public ViewEngines getView() {
        return viewEngines;
    }

    public void loadEngines() {
        wineVersionsManager.fetchAvailableWineVersions(versions -> Platform.runLater(() -> this.viewEngines.populate(versions)));
        this.viewEngines.showWineVersions();
    }
}
