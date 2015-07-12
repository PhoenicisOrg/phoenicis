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

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.messages.CancelerSynchronousMessage;
import com.playonlinux.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.messages.InterrupterSynchronousMessage;
import com.playonlinux.ui.ProgressControl;
import com.playonlinux.ui.SetupWindow;
import com.playonlinux.utils.OperatingSystem;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.gnome.gtk.Fixed;
import org.gnome.gtk.Window;
import org.gnome.gtk.WindowPosition;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SetupWindowGTKImplementation extends Window implements SetupWindow {
    private static final Logger LOGGER = Logger.getLogger(SetupWindowGTKImplementation.class);
    private final String title;
    private Fixed root;
    private byte[] topImage;
    private URL leftImage;

    public SetupWindowGTKImplementation(String title) {
        this.title = title;
        setTitle(title);
        setDefaultSize(520, 400);
        setPosition(WindowPosition.CENTER);

        try {
            this.loadImages();
        } catch (IOException e) {
            LOGGER.warn(e);
        }
        root = new Fixed();

        this.add(root);
        showAll();
    }

    private void loadImages() throws IOException {
        InputStream topImageStream
                = SetupWindow.class.getResourceAsStream("/com/playonlinux/ui/setupwindow/defaultTopImage.png");

        topImage = IOUtils.toByteArray(topImageStream);
        try {
            switch ( OperatingSystem.fetchCurrentOperationSystem() ) {
                case MACOSX:
                    this.leftImage = this.getClass().getSuperclass()
                            .getResource("/com/playonlinux/ui/setupwindow/defaultLeftPlayOnMac.jpg");
                    break;
                default:
                case LINUX:
                    this.leftImage = this.getClass().getSuperclass()
                            .getResource("/com/playonlinux/ui/setupwindow/defaultLeftPlayOnLinux.jpg");
                    break;
            }
        } catch (PlayOnLinuxException playOnLinuxException) {
            LOGGER.info("Unable to load a setupWindow image. Switching to the default one.", playOnLinuxException);
            this.leftImage = this.getClass().getSuperclass()
                    .getResource("/com/playonlinux/ui/setupwindow/defaultLeftPlayOnLinux.jpg");
        }
    }

    @Override
    public void setTopImage(File topImage) throws IOException {
        FileInputStream inputStream = new FileInputStream(topImage);
        this.topImage = IOUtils.toByteArray(inputStream);
    }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException {
        this.leftImage = new URL(leftImage.getAbsolutePath());
    }

    @Override
    public void showSimpleMessageStep(CancelerSynchronousMessage message, String textToShow) {
        StepRepresentationMessage stepMessage = new StepRepresentationMessage(this, message, textToShow);
        stepMessage.installStep();
        showAll();
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
        return null;
    }

    @Override
    public void showPresentationStep(CancelerSynchronousMessage message, String textToShow) {

    }

    @Override
    public void showLicenceStep(CancelerSynchronousMessage message, String textToShow, String licenceText) {

    }

    @Override
    public void close() {
        this.destroy();
    }

    public Fixed getRoot() {
        return root;
    }

    public byte[] getTopImage() {
        return topImage;
    }

    public URL getLeftImage() {
        return leftImage;
    }

    public String getWizardTitle() {
        return title;
    }
}
