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

package org.phoenicis.scripts.wizard;

import org.phoenicis.scripts.ui.SetupUiFactory;
import org.phoenicis.scripts.ui.UiMessageSender;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.Optional;

public class UiSetupWizardFactory {
    @Value("${user.home}")
    private String userHome;

    @Value("${application.user.root}")
    private String applicationUserRoot;

    @Value("${application.name}")
    private String applicationName;

    private final UiMessageSender uiMessageSender;
    private final SetupUiFactory setupUiFactory;

    /**
     * constructor
     * @param uiMessageSender
     * @param setupUiFactory
     */
    public UiSetupWizardFactory(UiMessageSender uiMessageSender, SetupUiFactory setupUiFactory) {
        this.uiMessageSender = uiMessageSender;
        this.setupUiFactory = setupUiFactory;
    }

    /**
     * creates a setup wizard
     * @param title title of the wizard
     * @param miniature miniature of the setup wizard (usually the miniature of the installed application)
     * @return created wizard
     */
    public UiSetupWizardImplementation create(String title, Optional<URI> miniature) {
        final UiSetupWizardImplementation uiSetupWizardImplementation = new UiSetupWizardImplementation(title,
                miniature,
                uiMessageSender,
                setupUiFactory,
                userHome,
                applicationUserRoot,
                applicationName);
        uiSetupWizardImplementation.init();
        return uiSetupWizardImplementation;
    }
}
