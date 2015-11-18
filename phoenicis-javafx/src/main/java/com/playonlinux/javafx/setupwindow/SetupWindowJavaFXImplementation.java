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

package com.playonlinux.javafx.setupwindow;

import static com.playonlinux.core.lang.Localisation.translate;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.playonlinux.core.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.core.messages.InterrupterSynchronousMessage;
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.javafx.mainwindow.library.ViewLibrary;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.ui.api.SetupWindow;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SetupWindowJavaFXImplementation extends Tab implements SetupWindow {
    private final Pane root;
    private final String wizardTitle;

    private URL topImage;
    private URL leftImage;

    private final ViewLibrary parentView;

    public SetupWindowJavaFXImplementation(String title, ViewLibrary parentView) {
        super();
        this.root = new Pane();

        this.wizardTitle = title;
        this.parentView = parentView;

        this.setText(translate(title));
        this.setContent(root);

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
        switch ( OperatingSystem.fetchCurrentOperationSystem() ) {
            case MACOSX:
                this.leftImage = this.getClass().getResource("defaultLeftPlayOnMac.jpg");
                break;
            default:
            case LINUX:
                this.leftImage = this.getClass().getResource("defaultLeftPlayOnLinux.jpg");
                break;
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
    public void showBrowseStep(CancelerSynchronousMessage message, String textToShow, File browseDirectory, List<String> extensions) {
        StepRepresentationBrowse stepRepresentationBrowse =
                new StepRepresentationBrowse(this, message, textToShow, browseDirectory, extensions);
        stepRepresentationBrowse.installStep();
    }

    @Override
    public void close() {
        this.parentView.closeTab(this);
    }

    @Override
    public void setTopImage(File topImage) throws MalformedURLException {
        this.topImage = new URL(topImage.getAbsolutePath());
    }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException {
        this.leftImage = new URL(leftImage.getAbsolutePath());
    }

    @Override
    public void setTopImage(URL topImage) throws IOException {
        this.topImage = topImage;
    }

    @Override
    public void setLeftImage(URL leftImage) throws IOException {
        this.leftImage = leftImage;
    }

    public URL getLeftImage() {
        return leftImage;
    }

    public URL getTopImage() {
        return topImage;
    }

    public Stage getParentWindow() {
        return this.parentView.getParentWindow();
    }
}
