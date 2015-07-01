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

package com.playonlinux.ui.impl.gtk.setupwindow;

import com.playonlinux.messages.CancelerSynchroneousMessage;
import com.playonlinux.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.messages.InterrupterSynchroneousMessage;
import com.playonlinux.ui.ProgressStep;
import com.playonlinux.ui.SetupWindow;
import org.gnome.gtk.Window;
import org.gnome.gtk.WindowPosition;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class SetupWindowGTKImplementation extends Window implements SetupWindow {
    private final String title;

    public SetupWindowGTKImplementation(String title) {
        this.title = title;
        setTitle(title);
        setDefaultSize(520, 400);
        setPosition(WindowPosition.CENTER);
        show();
    }

    @Override
    public void setTopImage(File topImage) throws MalformedURLException {

    }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException {

    }

    @Override
    public void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow) {

    }

    @Override
    public void showYesNoQuestionStep() {

    }

    @Override
    public void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue) {

    }

    @Override
    public void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems) {

    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {

    }

    @Override
    public ProgressStep showProgressBar(InterrupterSynchroneousMessage message, String textToShow) {
        return null;
    }

    @Override
    public void showPresentationStep(CancelerSynchroneousMessage message, String textToShow) {

    }

    @Override
    public void showLicenceStep(CancelerSynchroneousMessage message, String textToShow, String licenceText) {

    }

    @Override
    public void close() {

    }
}
