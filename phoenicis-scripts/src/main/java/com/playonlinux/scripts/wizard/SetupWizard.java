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

package com.playonlinux.scripts.wizard;

import com.playonlinux.scripts.interpreter.ScriptException;
import com.playonlinux.scripts.ui.ProgressControl;
import com.playonlinux.scripts.ui.SetupWindow;
import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.UIMessageSender;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static com.playonlinux.configuration.localisation.Localisation.translate;


public class SetupWizard implements CompleteWizard {
    private final String title;
    private final UIMessageSender messageSender;
    private final SetupWindowFactory setupWindowFactory;

    private SetupWindow setupWindow;

    private final String applicationUserRoot;

    private final String applicationName;

    /**
     * Create the setupWindow
     *
     * @param title title of the setupWindow
     */
    public SetupWizard(String title,
                       UIMessageSender messageSender,
                       SetupWindowFactory setupWindowFactory,
                       String applicationUserRoot,
                       String applicationName) {
        this.title = title;
        this.messageSender = messageSender;
        this.setupWindowFactory = setupWindowFactory;
        this.applicationUserRoot = applicationUserRoot;
        this.applicationName = applicationName;
    }

    /**
     * Creates the window
     */
    @Override
    public void init() {
        messageSender.run(() -> setupWindow = setupWindowFactory.createSetupWindow(title));
    }

    /**
     * Set the left image text
     *
     * @param leftImageText text for the left image
     */
    @Override
    public void setLeftImageText(String leftImageText) {
        setupWindow.setLeftImageText(leftImageText);
    }

    /**
     * Set the top image
     *
     * @param topImage URL of the top image
     */
    @Override
    public void setTopImage(String topImage) throws IOException {
        setupWindow.setTopImage(new File(topImage));
    }

    /**
     * Closes the setupWindow
     */
    @Override
    public void close() {
        messageSender.run(() -> {
            setupWindow.close();
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
        return messageSender.runAndWait(message -> setupWindow.showSimpleMessageStep(message, textToShow));
    }

    /**
     * Show a default script presentation
     *
     * @param programName   the name of the program
     * @param programEditor the editor of the program
     * @param applicationHomepage homepage of the application
     * @param scriptorName  the scriptor name
     */
    @Override
    public Void presentation(String programName, String programEditor, String applicationHomepage, String scriptorName) {
        final String textToShow = String.format(translate("This wizard will help you install \"%1$s\" on your computer.\n\n"
                        + "This program was created by: %2$s\n\n"
                        + "For more information about this program, visit:\n%3$s\n\n"
                        + "This installation program is provided by: %4$s\n\n"
                        + "\n\n%1$s will be installed in: %5$s\n\n"
                        + "%6$s is not responsible for anything that might happen as a result of using"
                        + " these scripts.\n\nClick Next to start")
                , programName, programEditor, applicationHomepage, scriptorName, applicationUserRoot, applicationName);

        return presentation(textToShow);
    }

    /**
     * Show a free script presentation
     *
     * @param textToShow the free presentation text to showRightView
     */
    @Override
    public Void presentation(String textToShow) {
        return messageSender.runAndWait(message -> setupWindow.showPresentationStep(message, textToShow));
    }

    /**
     * Show the content of a licence file
     *
     * @param textToShow  a message above the licence
     * @param licenceFile the licence file to display (with 'from java.io import File')
     */
    @Override
    public Void licenceFile(String textToShow, File licenceFile) {
        try {
            try(final FileInputStream content = new FileInputStream(licenceFile)) {
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
     * @param textToShow      a message above the licence
     * @param licenceFilePath the path of the licence file to display
     */
    @Override
    public Void licenceFile(String textToShow, String licenceFilePath) {
        return licenceFile(textToShow, new File(licenceFilePath));
    }

    /**
     * Show a custom licence message
     *
     * @param textToShow  a message above the licence
     * @param licenceText the licence text to showRightView
     */
    @Override
    public Void licence(String textToShow, String licenceText) {
        return messageSender.runAndWait(message -> setupWindow.showLicenceStep(message, textToShow, licenceText));
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
     * @param textToShow   a text that will be shown
     * @param defaultValue a default value
     * @return the value the user entered
     */
    @Override
    public String textbox(String textToShow, String defaultValue) {
        return messageSender.runAndWait(message -> setupWindow.showTextBoxStep(message, textToShow, defaultValue));
    }

    /**
     * Displays a showMenuStep so that the user can make a choice
     *
     * @param textToShow a text that will be shown
     * @param menuItems  a list containing the elements of the showMenuStep
     * @return the value the user entered (as string)
     */
    @Override
    public String menu(String textToShow, List<String> menuItems) {
        return messageSender.runAndWait(message -> setupWindow.showMenuStep(message, textToShow, menuItems));
    }

    /**
     * Asks the user to choose a file a file
     *
     * @param textToShow text to show
     * @return The path of the file
     */
    @Override
    public String browse(String textToShow) {
        return browse(textToShow, applicationUserRoot, null);
    }

    /**
     * Ask the user to choose a file
     *
     * @param textToShow        text to show
     * @param directory         default directory to browse in
     * @param allowedExtensions A list containing allowed extensions. All extensions will be allowed if this parameter
     *                          is set to null
     * @return The path of the file
     */
    @Override
    public String browse(String textToShow, String directory, List<String> allowedExtensions) {
        return messageSender.runAndWait(message -> setupWindow.showBrowseStep(message, textToShow,
                new File(directory), allowedExtensions));
    }


    /**
     * Displays a showSimpleMessageStep to the user with a waiting symbol, and releases the script just afterward
     *
     * @param textToShow a text that will be shown
     */
    @Override
    public Void wait(String textToShow) {
        return messageSender.runAndWait(message -> setupWindow.showSpinnerStep(message, textToShow));
    }

    @Override
    public ProgressControl progressBar(String textToShow) {
        return messageSender.runAndWait(message -> setupWindow.showProgressBar(message, textToShow));
    }

    @Override
    public String getTitle() {
        return title;
    }

}
