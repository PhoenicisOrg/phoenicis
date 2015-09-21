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

package com.playonlinux.ui.impl.javafx.mainwindow.containers;

import com.playonlinux.core.log.ScriptLogger;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.core.scripts.ScriptFailureException;
import com.playonlinux.framework.wizard.SetupWizardComponent;
import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.ui.api.ProgressControl;

import java.util.ArrayList;
import java.util.List;

class ContainerConfigurationMiniWizard implements WineWizard {
    private final ContainerConfigurationView<?> progressControl;
    private final List<SetupWizardComponent> setupWizardComponents = new ArrayList<>();

    ContainerConfigurationMiniWizard(ContainerConfigurationView<?> progressControl) {
        this.progressControl = progressControl;
    }

    @Override
    public String menu(String textToShow, List<String> menuItems) throws CancelException {
        return null;
        // TODO: Find a way to implement this method
    }

    @Override
    public ProgressControl progressBar(String textToShow) throws CancelException {
        return progressControl;
    }

    @Override
    public void init() {
        progressControl.showToolbar();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void registerComponent(SetupWizardComponent setupWizardComponent) {
        setupWizardComponents.add(setupWizardComponent);
    }

    @Override
    public void close() {
        progressControl.hideToolbar();
        for(SetupWizardComponent setupWizardComponent: setupWizardComponents) {
            setupWizardComponent.close();
        }
    }

    @Override
    public ScriptLogger getLogContext() throws ScriptFailureException {
        return null;
    }

    @Override
    public void log(String message) throws ScriptFailureException {

    }

    @Override
    public void log(String message, Throwable e) throws ScriptFailureException {

    }
}
