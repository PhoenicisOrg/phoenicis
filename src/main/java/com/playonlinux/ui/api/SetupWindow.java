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

package com.playonlinux.ui.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.playonlinux.core.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.core.messages.InterrupterSynchronousMessage;

/**
 * Represents a Setup Window.
 * This interfaces needs to be implemented by the UI.
 */
public interface SetupWindow {
    /**
     * Set the top image
     * @param topImage The top image to set
     * @throws IOException If the top image cannot be loaded
     */
    void setTopImage(File topImage) throws IOException;

    /**
     * Set the left image
     * @param leftImage The left image to set
     * @throws IOException If the left image cannot be loaded
     */
    void setLeftImage(File leftImage) throws IOException;

    /**
     * Set the top image
     * @param topImage The top image to set
     * @throws IOException If the top image cannot be loaded
     */
    void setTopImage(URL topImage) throws IOException;

    /**
     * Set the left image
     * @param leftImage The left image to set
     * @throws IOException If the left image cannot be loaded
     */
    void setLeftImage(URL leftImage) throws IOException;

    /**
     * Show a simple message and block until a response is given
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     */
    void showSimpleMessageStep(CancelerSynchronousMessage message, String textToShow);

    void showYesNoQuestionStep();

    /**
     * Show a a text box and block until a response is given
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     * @param defaultValue The default value
     */
    void showTextBoxStep(CancelerSynchronousMessage message, String textToShow, String defaultValue);

    /**
     * Show a menu step and block until a response is given
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     * @param menuItems The items in the menu
     */
    void showMenuStep(CancelerSynchronousMessage message, String textToShow, List<String> menuItems);

    /**
     * Show a wait message, and returns immediately
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     */
    void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow);

    /**
     * Show a progress bar and returns immediately
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     * @return a {@link ProgressControl} object that can be manipulated later
     */
    ProgressControl showProgressBar(InterrupterSynchronousMessage message, String textToShow);

    /**
     * Shows a presentation step and blocks until a response is given
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     */
    void showPresentationStep(CancelerSynchronousMessage message, String textToShow);

    /**
     * Shows a licence step and blocks until a response is given
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     * @param licenceText The licence content
     */
    void showLicenceStep(CancelerSynchronousMessage message, String textToShow, String licenceText);

    /**
     * Shows a file browser step, and blocks until a response is given
     * @param message The {@link com.playonlinux.core.messages.Message} object that will handle the response
     * @param textToShow The text to show to the user
     * @param browseDirectory The default directory to browse
     * @param extensions Allowed extensions
     */
    void showBrowseStep(CancelerSynchronousMessage message, String textToShow, File browseDirectory, List<String> extensions);

    /**
     * Close the SetupWindow
     */
    void close();
}
