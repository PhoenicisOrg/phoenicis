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

package com.playonlinux.ui.impl.javafx.setupwindow;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.messages.CancelerSynchronousMessage;
import com.playonlinux.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.messages.InterrupterSynchronousMessage;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.ui.impl.javafx.common.PlayOnLinuxScene;
import com.playonlinux.utils.OperatingSystem;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SetupWindowJavaFXImplementation extends Stage implements SetupWindow {
    private final Pane root;
    private final String wizardTitle;

    private URL topImage;
    private URL leftImage;

    private static final Logger LOGGER = Logger.getLogger(SetupWindowJavaFXImplementation.class);

    public SetupWindowJavaFXImplementation(String title) {
        super();
        this.root = new Pane();
        Scene scene = new PlayOnLinuxScene(root, 520, 400);
        scene.getStylesheets().add(this.getClass().getResource("setupWindow.css").toExternalForm());

        this.wizardTitle = title;

        this.setTitle(title);
        this.setScene(scene);
        this.show();

        this.loadImages();
    }

    public String getWizardTitle() {
        return wizardTitle;
    }

    public Pane getRoot() {
        return this.root;
    }

    public void clearAll() {
        root.getChildren().clear();
    }

    private void loadImages() {
        this.topImage = this.getClass().getResource("defaultTopImage.png");
        try {
            switch ( OperatingSystem.fetchCurrentOperationSystem() ) {
                case MACOSX:
                    this.leftImage = this.getClass().getResource("defaultLeftPlayOnMac.jpg");
                    break;
                default:
                case LINUX:
                    this.leftImage = this.getClass().getResource("defaultLeftPlayOnLinux.jpg");
                    break;
            }
        } catch (PlayOnLinuxException playOnLinuxException) {
            LOGGER.info("Unable to load a setupWindow image. Switching to the default one.", playOnLinuxException);
            this.leftImage = this.getClass().getResource("defaultLeftPlayOnLinux.jpg");
        }
    }


    public void addNode(Node widgetToAdd) {
        this.root.getChildren().add(widgetToAdd);
    }



    @Override
    public void showSimpleMessageStep(CancelerSynchronousMessage message, String textToShow) {
        StepRepresentationMessage stepMessage = new StepRepresentationMessage(this, message, textToShow);
        stepMessage.installStep();
    }

    @Override
    public void showYesNoQuestionStep() {
        // TODO
    }

    @Override
    public void showTextBoxStep(CancelerSynchronousMessage message, String textToShow, String defaultValue) {
        StepRepresentationTextBox stepTextBox = new StepRepresentationTextBox(this, message, textToShow, defaultValue);
        stepTextBox.installStep();
    }

    @Override
    public void showMenuStep(CancelerSynchronousMessage message, String textToShow, List<String> menuItems) {
        StepRepresentationMenu stepMenu = new StepRepresentationMenu(this, message, textToShow, menuItems);
        stepMenu.installStep();
    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {
        StepRepresentationSpin stepSpin = new StepRepresentationSpin(this, message, textToShow);
        stepSpin.installStep();
    }

    @Override
    public ProgressControl showProgressBar(InterrupterSynchronousMessage message, String textToShow) {
        StepRepresentationProgressBar stepProgressBar = new StepRepresentationProgressBar(this, message, textToShow);
        stepProgressBar.installStep();
        return stepProgressBar;
    }

    @Override
    public void showPresentationStep(CancelerSynchronousMessage message, String textToShow) {
        StepRepresentationPresentation stepRepresentationPresentation =
                new StepRepresentationPresentation(this, message, textToShow);
        stepRepresentationPresentation.installStep();
    }

    @Override
    public void showLicenceStep(CancelerSynchronousMessage message, String textToShow, String licenceText) {
        StepRepresentationLicence stepRepresentationLicence =
                new StepRepresentationLicence(this, message, textToShow, licenceText);
        stepRepresentationLicence.installStep();
    }


    @Override
    public void setTopImage(File topImage) throws MalformedURLException {
        this.topImage = new URL(topImage.getAbsolutePath());
    }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException {
        this.leftImage = new URL(leftImage.getAbsolutePath());
    }

    public URL getLeftImage() {
        return leftImage;
    }

    public URL getTopImage() {
        return topImage;
    }
}
