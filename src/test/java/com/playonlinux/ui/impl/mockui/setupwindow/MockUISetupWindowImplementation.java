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

import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.playonlinux.core.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.core.messages.InterrupterSynchronousMessage;

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
    public void showSimpleMessageStep(CancelerSynchronousMessage message, String textToShow) {
        assertEquals("Text to showRightView", textToShow);
        message.setResponse(null);
    }

    @Override
    public void showYesNoQuestionStep() {
        // TODO
    }

    @Override
    public void showTextBoxStep(CancelerSynchronousMessage message, String textToShow, String defaultValue) {
        assertEquals("Text to showRightView", textToShow);
        assertEquals("Default value", defaultValue);
        message.setResponse("showTextBoxStep result");
    }

    @Override
    public void showMenuStep(CancelerSynchronousMessage message, String textToShow, List<String> menuItems) {
        assertEquals("Text to showRightView", textToShow);
        assertEquals("Element 1", menuItems.get(0));
        assertEquals("Element 2", menuItems.get(1));

        message.setResponse("menu result");
    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {
        assertEquals("Text to showRightView", textToShow);
    }

    @Override
    public ProgressControl showProgressBar(InterrupterSynchronousMessage message, String textToShow) {
        return null;
    }

    @Override
    public void showPresentationStep(CancelerSynchronousMessage message, String textToShow) {
        // TODO
    }

    @Override
    public void close() {
        // Nothing to do to close the mockup, contrary to the real setup wizard
    }

    @Override
    public void showLicenceStep(CancelerSynchronousMessage message, String textToShow, String licenceText) {
        // TODO
        
    }
}
