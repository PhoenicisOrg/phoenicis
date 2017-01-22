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

package com.playonlinux.scripts.wizard;

import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.UIMessageSender;
import org.springframework.beans.factory.annotation.Value;

public class SetupWizardFactory {
    @Value("${user.home}")
    private String userHome;

    @Value("${application.user.root}")
    private String applicationUserRoot;

    @Value("${application.name}")
    private String applicationName;


    private final UIMessageSender uiMessageSender;
    private final SetupWindowFactory setupWindowFactory;

    public SetupWizardFactory(UIMessageSender uiMessageSender, SetupWindowFactory setupWindowFactory) {
        this.uiMessageSender = uiMessageSender;
        this.setupWindowFactory = setupWindowFactory;
    }

    public SetupWizard create(String title) {
        final SetupWizard setupWizard = new SetupWizard(title, uiMessageSender, setupWindowFactory, userHome, applicationUserRoot, applicationName);
        setupWizard.init();
        return setupWizard;
    }
}
