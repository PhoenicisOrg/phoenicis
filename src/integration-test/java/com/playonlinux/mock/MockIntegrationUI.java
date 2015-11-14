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

package com.playonlinux.mock;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.playonlinux.core.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.core.messages.InterrupterSynchronousMessage;
import com.playonlinux.ui.api.ProgressControl;

public class MockIntegrationUI implements com.playonlinux.ui.api.SetupWindow {
    @Override
    public void setTopImage(File topImage) throws IOException {

    }

    @Override
    public void setLeftImage(File leftImage) throws IOException {

    }

    @Override
    public void setTopImage(URL topImage) throws IOException {

    }

    @Override
    public void setLeftImage(URL leftImage) throws IOException {

    }

    @Override
    public void showSimpleMessageStep(CancelerSynchronousMessage message, String textToShow) {

    }

    @Override
    public void showYesNoQuestionStep() {

    }

    @Override
    public void showTextBoxStep(CancelerSynchronousMessage message, String textToShow, String defaultValue) {

    }

    @Override
    public void showMenuStep(CancelerSynchronousMessage message, String textToShow, List<String> menuItems) {

    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {

    }

    @Override
    public ProgressControl showProgressBar(InterrupterSynchronousMessage message, String textToShow) {
        return new MockProgressControl(textToShow);
    }

    @Override
    public void showPresentationStep(CancelerSynchronousMessage message, String textToShow) {

    }

    @Override
    public void showLicenceStep(CancelerSynchronousMessage message, String textToShow, String licenceText) {

    }

    @Override
    public void showBrowseStep(CancelerSynchronousMessage message, String textToShow, File browseDirectory,
            List<String> extensions) {

    }

    @Override
    public void close() {

    }
}
