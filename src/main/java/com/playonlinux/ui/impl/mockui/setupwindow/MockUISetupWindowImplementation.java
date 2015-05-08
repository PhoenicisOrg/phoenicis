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

package com.playonlinux.ui.impl.mockui.setupwindow;

import com.playonlinux.api.ProgressStep;
import com.playonlinux.api.SetupWindow;
import com.playonlinux.utils.messages.CancelerSynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterSynchroneousMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MockUISetupWindowImplementation implements SetupWindow {

    @Override
    public void setTopImage(File topImage) throws MalformedURLException { }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException { }

    @Override
    public void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow) {
        assertEquals("Text to show", textToShow);
        message.setResponse(null);
    }

    @Override
    public void showYesNoQuestionStep() {

    }

    @Override
    public void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue) {
        assertEquals("Text to show", textToShow);
        assertEquals("Default value", defaultValue);
        message.setResponse("showTextBoxStep result");
    }

    @Override
    public void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems) {
        assertEquals("Text to show", textToShow);
        assertEquals("Element 1", menuItems.get(0));
        assertEquals("Element 2", menuItems.get(1));

        message.setResponse("menu result");
    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {
        assertEquals("Text to show", textToShow);
    }

    @Override
    public ProgressStep showProgressBar(InterrupterSynchroneousMessage message, String textToShow) {
        return null;
    }

    @Override
    public void showPresentationStep(CancelerSynchroneousMessage message, String textToShow) {

    }

    @Override
    public void close() {

    }
}
