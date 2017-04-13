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

package org.phoenicis.javafx.controller.apps;

import javafx.application.Platform;
import org.phoenicis.apps.RepositoryManager;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.apps.ViewApps;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.settings.SettingsManager;

public class AppsController {
    private final ViewApps view;
    private final RepositoryManager repositoryManager;
    private final ScriptInterpreter scriptInterpreter;
    
    private Runnable onAppLoaded = () -> {};

    public AppsController(ViewApps view,
                          RepositoryManager repositoryManager,
                          ScriptInterpreter scriptInterpreter) {
        this.view = view;
        this.repositoryManager = repositoryManager;
        this.scriptInterpreter = scriptInterpreter;

        this.repositoryManager.addCallbacks(view::populate, e -> view.showFailure());
    }

    public void loadApps() {
        this.view.showWait();
        this.repositoryManager.triggerRepositoryChange();

        this.view.setOnRetryButtonClicked(event -> {
            this.view.showWait();
            this.repositoryManager.triggerRepositoryChange();
        });

        this.view.setOnSelectScript(scriptDTO -> scriptInterpreter.runScript(
                scriptDTO.getScript(),
                e -> Platform.runLater(() -> {
                    // no exception if installation is cancelled
                    if (!(e.getCause() instanceof InterruptedException)) {
                        new ErrorMessage("The script ended unexpectedly", e);
                    }
                })
        ));

        onAppLoaded.run();
    }

    public void setOnAppLoaded(Runnable onAppLoaded) {
        this.onAppLoaded = onAppLoaded;
    }

    public ViewApps getView() {
        return view;
    }
}
