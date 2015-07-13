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

import com.playonlinux.messages.CancelerSynchronousMessage;
import com.playonlinux.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.messages.InterrupterSynchronousMessage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SetupWindow {
    void setTopImage(File topImage) throws IOException;

    void setLeftImage(File leftImage) throws IOException;

    void showSimpleMessageStep(CancelerSynchronousMessage message, String textToShow);

    void showYesNoQuestionStep();

    void showTextBoxStep(CancelerSynchronousMessage message, String textToShow, String defaultValue);

    void showMenuStep(CancelerSynchronousMessage message, String textToShow, List<String> menuItems);

    void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow);

    ProgressControl showProgressBar(InterrupterSynchronousMessage message, String textToShow);

    void showPresentationStep(CancelerSynchronousMessage message, String textToShow);

    void showLicenceStep(CancelerSynchronousMessage message, String textToShow, String licenceText);

    void close();
}
