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

package org.phoenicis.scripts.wizard;

import org.apache.commons.io.IOUtils;
import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.ui.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class UiSetupWizardImplementation implements SetupWizard {
    private final String title;
    private final InstallationType installationType;
    private Optional<URI> miniature;
    private final UiMessageSender messageSender;
    private final SetupUiFactory setupUiFactory;

    private SetupUi setupUi;

    private final String userHome;

    private final String applicationUserRoot;

    private final String applicationName;

    /**
     * constructor
     * @param title wizard title
     * @param miniature miniature for the setup wizard (usually miniature of the application which shall be installed)
     * @param installationType apps/engines
     * @param messageSender
     * @param setupUiFactory
     * @param userHome
     * @param applicationUserRoot
     * @param applicationName Phoenicis PlayOnLinux/PlayOnMac
     */
    public UiSetupWizardImplementation(
            String title,
            Optional<URI> miniature,
            InstallationType installationType,
            UiMessageSender messageSender,
            SetupUiFactory setupUiFactory,
            String userHome,
            String applicationUserRoot,
            String applicationName) {
        this.title = title;
        this.miniature = miniature;
        this.installationType = installationType;
        this.messageSender = messageSender;
        this.setupUiFactory = setupUiFactory;
        this.userHome = userHome;
        this.applicationUserRoot = applicationUserRoot;
        this.applicationName = applicationName;
    }

    /**
     * Creates the window
     */
    @Override
    public void init() {
        messageSender.run(
                () -> setupUi = setupUiFactory.createSetupWindow(this.title, this.miniature, this.installationType));
    }

    /**
     * Set the left image text
     *
     * @param leftImageText text for the left image
     */
    @Override
    public void setLeftImageText(String leftImageText) {
        setupUi.setLeftImageText(leftImageText);
    }

    /**
     * Set the top image
     *
     * @param topImage URL of the top image
     */
    @Override
    public void setTopImage(String topImage) throws IOException {
        setupUi.setTopImage(new File(topImage));
    }

    /**
     * Closes the setupUi
     */
    @Override
    public void close() {
        messageSender.run(() -> {
            setupUi.close();
            return null;
        });
    }

    /**
     * Shows a simple showSimpleMessageStep
     *
     * @param textToShow the text to showRightView
     */
    @Override
    public Void message(String textToShow) {
        return messageSender.runAndWait(message -> setupUi.showSimpleMessageStep(message, textToShow));
    }

    /**
     * Show a free script presentation
     *
     * @param htmlToShow the free presentation HTML to showRightView
     */
    @Override
    public Void presentationHtml(String htmlToShow) {
        return messageSender.runAndWait(message -> setupUi.showHtmlPresentationStep(message, htmlToShow));
    }

    /**
     * Show a default script presentation
     *
     * @param programName the name of the program
     * @param programEditor the editor of the program
     * @param applicationHomepage homepage of the application
     * @param scriptorName the scriptor name
     */
    @Override
    public Void presentation(String programName, String programEditor, String applicationHomepage,
            String scriptorName) {
        final String htmlToShow = "<body>" + tr("Installation wizard for {0}", programName)
                + ".<br><br>" + tr("Program by {0}", programEditor) + "<br><br>"
                + tr("For more information about this program, visit:")
                + String.format("<br><a href=\"%1$s\">%1$s</a><br><br>", applicationHomepage)
                + tr("Installation script by {0}", scriptorName) + "<br><br>" + "<br><br>"
                + tr("{0} will be installed in: {1}", programName, applicationUserRoot) + "<br><br>"
                + tr("{0} is not responsible for anything that might happen as a result of using these scripts.",
                        applicationName)
                + "<br><br>" + tr("Click Next to start.") + "</body>";
        return messageSender.runAndWait(message -> setupUi.showHtmlPresentationStep(message, htmlToShow));
    }

    /**
     * Show a free script presentation
     *
     * @param textToShow the free presentation text to showRightView
     */
    @Override
    public Void presentation(String textToShow) {
        return messageSender.runAndWait(message -> setupUi.showPresentationStep(message, textToShow));
    }

    /**
     * Show the content of a licence file
     *
     * @param textToShow a message above the licence
     * @param licenceFile the licence file to display (with 'from java.io import File')
     */
    @Override
    public Void licenceFile(String textToShow, File licenceFile) {
        try {
            try (final FileInputStream content = new FileInputStream(licenceFile)) {
                final StringWriter writer = new StringWriter();
                IOUtils.copy(content, writer, "UTF-8");
                return licence(textToShow, writer.toString());
            }
        } catch (IOException e) {
            throw new ScriptException("Cannot acces the licence file", e);
        }
    }

    /**
     * Show the content of a licence file
     *
     * @param textToShow a message above the licence
     * @param licenceFilePath the path of the licence file to display
     */
    @Override
    public Void licenceFile(String textToShow, String licenceFilePath) {
        return licenceFile(textToShow, new File(licenceFilePath));
    }

    /**
     * Show a custom licence message
     *
     * @param textToShow a message above the licence
     * @param licenceText the licence text to showRightView
     */
    @Override
    public Void licence(String textToShow, String licenceText) {
        return messageSender.runAndWait(message -> setupUi.showLicenceStep(message, textToShow, licenceText));
    }

    /**
     * Ask the user to enter a value
     *
     * @param textToShow a text that will be shown
     * @return the value the user entered
     */
    @Override
    public String textbox(String textToShow) {
        return this.textbox(textToShow, "");
    }

    /**
     * Asks the user to enter a value
     *
     * @param textToShow a text that will be shown
     * @param defaultValue a default value
     * @return the value the user entered
     */
    @Override
    public String textbox(String textToShow, String defaultValue) {
        return messageSender.runAndWait(message -> setupUi.showTextBoxStep(message, textToShow, defaultValue));
    }

    /**
     * Displays a showMenuStep so that the user can make a choice
     *
     * @param textToShow a text that will be shown
     * @param menuItems a list containing the elements of the showMenuStep
     * @return the value the user entered (as string)
     */
    @Override
    public MenuItem menu(String textToShow, List<String> menuItems) {
        return this.menu(textToShow, menuItems, "");
    }

    /**
     * Displays a showMenuStep so that the user can make a choice
     *
     * @param textToShow a text that will be shown
     * @param menuItems a list containing the elements of the showMenuStep
     * @param defaultValue item which is selected by default
     * @return the value the user entered (as string)
     */
    @Override
    public MenuItem menu(String textToShow, List<String> menuItems, String defaultValue) {
        return messageSender.runAndWait(message -> setupUi.showMenuStep(message, textToShow, menuItems, defaultValue));
    }

    /**
     * Asks the user to choose a file a file
     *
     * @param textToShow text to show
     * @return The path of the file
     */
    @Override
    public String browse(String textToShow) {
        return browse(textToShow, this.userHome, null);
    }

    /**
     * Ask the user to choose a file
     *
     * @param textToShow text to show
     * @param directory default directory to browse in
     * @param allowedExtensions A list containing allowed extensions. All extensions will be allowed if this parameter
     *            is set to null
     * @return The path of the file
     */
    @Override
    public String browse(String textToShow, String directory, List<String> allowedExtensions) {
        return messageSender.runAndWait(
                message -> setupUi.showBrowseStep(message, textToShow, new File(directory), allowedExtensions));
    }

    /**
     * Displays a showSimpleMessageStep to the user with a waiting symbol, and releases the script just afterward
     *
     * @param textToShow a text that will be shown
     */
    @Override
    public Void wait(String textToShow) {
        return messageSender.runAndWait(message -> setupUi.showSpinnerStep(message, textToShow));
    }

    @Override
    public ProgressControl progressBar(String textToShow) {
        return messageSender.runAndWait(message -> setupUi.showProgressBar(message, textToShow));
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BrowserControl createBrowser(String textToShow) {
        return messageSender.runAndWait(message -> setupUi.showBrowser(message, textToShow));
    }
}
