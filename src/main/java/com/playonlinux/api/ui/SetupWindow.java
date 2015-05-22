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

package com.playonlinux.api.ui;

import com.playonlinux.utils.messages.CancelerSynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterSynchroneousMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public interface SetupWindow {
    void setTopImage(File topImage) throws MalformedURLException;

    void setLeftImage(File leftImage) throws MalformedURLException;

    void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow);

    void showYesNoQuestionStep();

    void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue);

    void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems);

    void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow);

    ProgressStep showProgressBar(InterrupterSynchroneousMessage message, String textToShow);

    void showPresentationStep(CancelerSynchroneousMessage message, String textToShow);

    void close();
}
