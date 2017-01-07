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

package com.playonlinux.javafx.views.setupwindow;

import com.playonlinux.scripts.ui.Message;
import com.playonlinux.scripts.ui.ProgressControl;
import com.playonlinux.scripts.ui.SetupWindow;
import com.playonlinux.tools.system.OperatingSystemFetcher;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class SetupWindowJavaFXImplementation extends Tab implements SetupWindow {
    private final BorderPane root;
    private final String wizardTitle;

    private URL topImage;
    private URL leftImage;
    private Runnable onShouldClose = () -> {};
    private final OperatingSystemFetcher operatingSystemFetcher;

    public SetupWindowJavaFXImplementation(String title, OperatingSystemFetcher operatingSystemFetcher) {
        super();
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.root = new BorderPane();

        this.wizardTitle = title;

        this.setText(translate(title));
        this.setContent(root);

        this.loadImages();
    }

    public String getWizardTitle() {
        return wizardTitle;
    }

    public BorderPane getRoot() {
        return this.root;
    }

    public void clearAll() {
        root.getChildren().clear();
    }

    private void loadImages() {
        this.topImage = this.getClass().getResource("defaultTopImage.png");
        switch ( operatingSystemFetcher.fetchCurrentOperationSystem() ) {
            case MACOSX:
                this.leftImage = this.getClass().getResource("defaultLeftPlayOnMac.jpg");
                break;
            case LINUX:
            default:
                this.leftImage = this.getClass().getResource("defaultLeftPlayOnLinux.jpg");
                break;
        }
    }


    public void addNode(Node widgetToAdd) {
        this.root.getChildren().add(widgetToAdd);
    }

    @Override
    public void showSimpleMessageStep(Message message, String textToShow) {
        StepRepresentationMessage stepMessage = new StepRepresentationMessage(this, message, textToShow);
        stepMessage.installStep();
    }

    @Override
    public void showYesNoQuestionStep() {
        // TODO
    }

    @Override
    public void showTextBoxStep(Message<String> message, String textToShow, String defaultValue) {
        StepRepresentationTextBox stepTextBox = new StepRepresentationTextBox(this, message, textToShow, defaultValue);
        stepTextBox.installStep();
    }

    @Override
    public void showMenuStep(Message<String> message, String textToShow, List<String> menuItems) {
        StepRepresentationMenu stepMenu = new StepRepresentationMenu(this, message, textToShow, menuItems);
        stepMenu.installStep();
    }

    @Override
    public void showSpinnerStep(Message<Void> message, String textToShow) {
        StepRepresentationSpin stepSpin = new StepRepresentationSpin(this, message, textToShow);
        stepSpin.installStep();
        message.send(null);
    }

    @Override
    public void showProgressBar(Message<ProgressControl> message, String textToShow) {
        StepRepresentationProgressBar stepProgressBar = new StepRepresentationProgressBar(this, message, textToShow);
        stepProgressBar.installStep();
        message.send(stepProgressBar);
    }

    @Override
    public void showPresentationStep(Message<Void> message, String textToShow) {
        StepRepresentationPresentation stepRepresentationPresentation =
                new StepRepresentationPresentation(this, message, textToShow);
        stepRepresentationPresentation.installStep();
    }

    @Override
    public void showLicenceStep(Message<Void> message, String textToShow, String licenceText) {
        StepRepresentationLicence stepRepresentationLicence =
                new StepRepresentationLicence(this, message, textToShow, licenceText);
        stepRepresentationLicence.installStep();
    }

    @Override
    public void showBrowseStep(Message<String> message, String textToShow, File browseDirectory, List<String> extensions) {
        StepRepresentationBrowse stepRepresentationBrowse =
                new StepRepresentationBrowse(this, message, textToShow, browseDirectory, extensions);
        stepRepresentationBrowse.installStep();
    }

    @Override
    public void close() {
        onShouldClose.run();
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

    public void setOnShouldClose(Runnable onShouldClose) {
        this.onShouldClose = onShouldClose;
    }
}
