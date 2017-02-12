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

package org.phoenicis.tests;

import org.phoenicis.scripts.ui.MenuItem;
import org.phoenicis.scripts.ui.Message;
import org.phoenicis.scripts.ui.ProgressControl;
import org.phoenicis.scripts.ui.SetupWindow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

class TestSetupWindow implements SetupWindow {
    @Override
    public void setTopImage(File topImage) throws IOException {

    }

    @Override
    public void setLeftImageText(String leftImageText) {

    }

    @Override
    public void setTopImage(URL topImage) throws IOException {

    }

    @Override
    public void showSimpleMessageStep(Message<Void> doneCallback, String textToShow) {
        doneCallback.send(null);
    }

    @Override
    public void showYesNoQuestionStep() {

    }

    @Override
    public void showTextBoxStep(Message<String> doneCallback, String textToShow, String defaultValue) {
        doneCallback.send("");
    }

    @Override
    public void showMenuStep(Message<MenuItem> doneCallback, String textToShow, List<String> menuItems, String defaultValue) {
        doneCallback.send(null);
    }

    @Override
    public void showSpinnerStep(Message<Void> message, String textToShow) {
        message.send(null);
    }

    @Override
    public void showProgressBar(Message<ProgressControl> message, String textToShow) {
        message.send(new ProgressControl() {
            @Override
            public void setProgressPercentage(double value) {

            }

            @Override
            public void setText(String text) {

            }
        });
    }

    @Override
    public void showPresentationStep(Message<Void> doneCallback, String textToShow) {
        doneCallback.send(null);
    }

    @Override
    public void showLicenceStep(Message<Void> doneCallback, String textToShow, String licenceText) {
        doneCallback.send(null);
    }

    @Override
    public void showBrowseStep(Message<String> doneCallback, String textToShow, File browseDirectory, List<String> extensions) {
        doneCallback.send(null);
    }

    @Override
    public void close() {

    }
}
