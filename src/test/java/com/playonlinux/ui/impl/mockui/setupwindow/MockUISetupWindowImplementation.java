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

import com.playonlinux.common.api.ui.ProgressStep;
import com.playonlinux.common.api.ui.SetupWindow;
import com.playonlinux.common.messages.CancelerSynchroneousMessage;
import com.playonlinux.common.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.common.messages.InterrupterSynchroneousMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MockUISetupWindowImplementation implements SetupWindow {

    @Override
    public void setTopImage(File topImage) throws MalformedURLException {
        // Nothing to do here, because the mockup has no images
    }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException {
        // Nothing to do here, because the mockup has no images
    }

    @Override
    public void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow) {
        assertEquals("Text to show", textToShow);
        message.setResponse(null);
    }

    @Override
    public void showYesNoQuestionStep() {
        // TODO
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
        // TODO
    }

    @Override
    public void close() {
        // Nothing to do to close the mockup, contrary to the real setup wizard
    }

    @Override
    public void showLicenceStep(CancelerSynchroneousMessage message, String textToShow, String licenceText) {
        // TODO
        
    }
}
