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

import org.phoenicis.scripts.ui.ProgressControl;
import org.phoenicis.scripts.ui.ProgressUi;
import org.phoenicis.scripts.ui.ProgressUiFactory;
import org.phoenicis.scripts.ui.UiMessageSender;

public class UiProgressWizardImplementation implements ProgressWizard {
    private String title;
    private final UiMessageSender messageSender;
    private final ProgressUiFactory progressUiFactory;

    private ProgressUi progressUi;

    /**
     * Create the progress UI
     *
     * @param messageSender
     * @param progressUiFactory
     */
    public UiProgressWizardImplementation(String title,
                                          UiMessageSender messageSender,
                                          ProgressUiFactory progressUiFactory) {
        this.title = title;
        this.messageSender = messageSender;
        this.progressUiFactory = progressUiFactory;
    }

    /**
     * Creates the window
     */
    @Override
    public void init() {
        messageSender.run(() -> progressUi = progressUiFactory.create(title));
    }


    /**
     * Closes the progress UI
     */
    @Override
    public void close() {
        messageSender.run(() -> {
            progressUi.close();
            return null;
        });
    }

    @Override
    public ProgressControl progressBar(String textToShow) {
        return messageSender.runAndWait(message -> progressUi.showProgressBar(message, textToShow));
    }

    @Override
    public String getTitle() {
        return title;
    }

}
