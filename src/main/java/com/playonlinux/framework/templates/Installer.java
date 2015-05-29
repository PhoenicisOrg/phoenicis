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

import com.playonlinux.domain.ScriptTemplate;
import com.playonlinux.framework.SetupWizard;

public abstract class Installer implements ScriptTemplate {
    /* Template parameters */
    private String logContext;
    private String title;

    /* Template attributes */
    private String setupWizardTitle;
    protected SetupWizard setupWizard;

    /* Private methods */
    public void _setSetupWizardTitle(String setupWizardTitle) {
        this.setupWizardTitle = setupWizardTitle;
    }

    public void _defaultRollback() {
        if(this.setupWizard != null) {
            setupWizard.close();
        }
    }

    /* Methods that can be overwritten */
    public abstract void main();

    public void rollback() {
        this._defaultRollback();
    }

    /* Methods that can be called */
    protected SetupWizard getSetupWizard() {
        if(this.setupWizard == null) {
            setupWizard = new SetupWizard(setupWizardTitle);
        }
        return setupWizard;
    }
}
