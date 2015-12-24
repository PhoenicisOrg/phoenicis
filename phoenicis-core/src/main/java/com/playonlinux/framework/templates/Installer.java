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

package com.playonlinux.framework.templates;

import com.playonlinux.core.python.PythonAttribute;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.core.scripts.ScriptFailureException;
import com.playonlinux.framework.SetupWizard;

/**
 * Most abstract {@link ScriptTemplate}
 */
public abstract class Installer implements ScriptTemplate {
    @PythonAttribute
    private String title;

    /* Template attributes */
    protected SetupWizard setupWizard;
    private boolean setupWizardInitialized = false;

    public void _defaultRollback() {
        if(this.setupWizard != null) {
            setupWizard.close();
        }
    }

    @Override
    public abstract void main() throws CancelException;

    @Override
    public void rollback() {
        this._defaultRollback();
    }

    /* Methods that can be called */
    protected SetupWizard setupWizard() {
        createSetupWizard();
        initalizeSetupWizard();
        return setupWizard;
    }

    private void initalizeSetupWizard() {
        if(!setupWizardInitialized) {
            setupWizard.init();
            setupWizardInitialized = true;
        }
    }

    public void log(String message) throws ScriptFailureException {
        createSetupWizard();
        setupWizard.log(message);
    }

    private void createSetupWizard() {
        if(this.setupWizard == null) {
            setupWizard = new SetupWizard(title);
            setupWizardInitialized = false;
        }
    }
}