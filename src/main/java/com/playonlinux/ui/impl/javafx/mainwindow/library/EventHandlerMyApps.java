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

package com.playonlinux.ui.impl.javafx.mainwindow.library;

import com.playonlinux.services.EventHandler;
import com.playonlinux.services.InstalledApplications;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.UIEventHandler;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static com.playonlinux.lang.Localisation.translate;

@Scan
class EventHandlerMyApps implements UIEventHandler {
    @Inject
    static EventHandler mainEventHandler;

    private Logger logger = Logger.getLogger(this.getClass());

    public InstalledApplications getInstalledApplications() throws PlayOnLinuxException {
        return mainEventHandler.getInstalledApplications();
    }

    public void runLocalScript(File scriptToRun) throws IOException {
        mainEventHandler.runLocalScript(scriptToRun);
    }

    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }

    public void onApplicationStarted() throws MalformedURLException {
        mainEventHandler.onApplicationStarted();
    }

    public void runApplication(String applicationName) {
        try {
            mainEventHandler.runApplication(applicationName);
        } catch (PlayOnLinuxException e) {
            logger.error(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to run the application."));
            alert.setContentText(String.format("The error was: %s", e));
            alert.show();
        }
    }


}
