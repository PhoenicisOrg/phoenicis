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

package com.playonlinux.framework;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.core.log.LogStream;
import com.playonlinux.core.log.LogStreamFactory;
import com.playonlinux.core.messages.*;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.ui.api.UIMessageSender;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;

@Scan
public class SetupWizard {

    @Inject
    private static Controller controller;

    @Inject
    private static LogStreamFactory logStreamFactory;

    private final String title;

    WeakReference<SetupWindow> setupWindow;
    UIMessageSender<String> messageSender;
    private LogStream logContext;


    /**
     * Create the setupWindow
     *
     * @param title title of the setupWindow
     */
    public SetupWizard(String title) {
        this.messageSender = controller.createUIMessageSender();
        this.title = title;

        messageSender.synchronousSend(
                new SynchronousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow = new WeakReference<>(controller.createSetupWindowGUIInstance(translate(title)));
                    }
                }
        );
    }

    /**
     * Closes the setupWindow
     */
    public void close() {
        messageSender.synchronousSend(
                new SynchronousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().close();
                    }
                }
        );
    }

    /**
     * Shows a simple showSimpleMessageStep
     * @param textToShow the text to showRightView
     * @throws CancelException
     */
    public void message(String textToShow) throws CancelException {
        messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().showSimpleMessageStep((CancelerSynchronousMessage) message, textToShow);
                    }
                }
        );
    }

    /**
     * Show a default script presentation
     * @param programName the name of the program
     * @param programEditor the editor of the program
     * @param editorURL the editor website URL
     * @param scriptorName the scriptor name
     * @param prefixName the name of the prefix for that program
     * @throws CancelException
     */
    public void presentation(String programName, String programEditor, String editorURL, String scriptorName, String prefixName) throws CancelException {
        final String textToShow = String.format(translate("This wizard will help you install %1$s on your computer.\n\n"
                + "This program was created by: %2$s\n%3$s\n\nThis installation program is provided by: %4$s"
                + "\n\n%1$s will be installed in: ${application.user.wineprefix}%5$s\n\n"
                + "${application.name} is not responsible for anything that might happen as a result of using"
                + " these scripts.\n\nClick Next to start")
                , programName, programEditor, editorURL, scriptorName, prefixName);
        presentation(textToShow);
    }

    /**
     * Show a free script presentation
     * @param textToShow the free presentation text to showRightView
     * @throws CancelException
     */
    public void presentation(String textToShow) throws CancelException {
        messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().showPresentationStep((CancelerSynchronousMessage) message, textToShow);
                    }
                }
        );
    }

    /**
     * Show the content of a licence file
     * @param textToShow a message above the licence
     * @param licenceFile the licence file to display (with 'from java.io import File')
     * @throws CancelException
     */
    public void licenceFile(String textToShow, File licenceFile) throws CancelException {
        try {
            final FileInputStream content = new FileInputStream(licenceFile);
            final StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            content.close();
            licence(textToShow, writer.toString());
        } catch (IOException e) {
            throw new ScriptFailureException("Cannot acces the licence file", e);
        }
    }

    /**
     * Show the content of a licence file
     * @param textToShow a message above the licence
     * @param licenceFilePath the path of the licence file to display
     * @throws ScriptFailureException
     * @throws CancelException
     */
    public void licenceFile(String textToShow, String licenceFilePath) throws CancelException {
        licenceFile(textToShow, new File(licenceFilePath));
    }

    /**
     * Show a custom licence message
     * @param textToShow a message above the licence
     * @param licenceText the licence text to showRightView
     * @throws CancelException
     */
    public String licence(String textToShow, String licenceText) throws CancelException {
        return (String) messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().showLicenceStep((CancelerSynchronousMessage) message, textToShow, licenceText);
                    }
                }
        );
    }
    
    /**
     * Ask the user to enter a value
     * @param textToShow a text that will be shown
     * @return the value the user entered
     * @throws CancelException
     */
    public String textbox(String textToShow) throws CancelException {
        return this.textbox(textToShow, "");
    }

    /**
     * Asks the user to enter a value
     * @param textToShow a text that will be shown
     * @param defaultValue a default value
     * @return the value the user entered
     * @throws CancelException
     */
    public String textbox(String textToShow, String defaultValue) throws CancelException {
        return (String) messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().showTextBoxStep((CancelerSynchronousMessage) message, textToShow, defaultValue);
                    }
                }
        );
    }

    /**
     * Displays a showMenuStep so that the user can make a choice
     * @param textToShow a text that will be shown
     * @param menuItems a list containing the elements of the showMenuStep
     * @return the value the user entered (as string)
     * @throws CancelException
     */
    public String menu(String textToShow, List<String> menuItems) throws CancelException {
        return (String) messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().showMenuStep((CancelerSynchronousMessage) message, textToShow, menuItems);
                    }
                }
        );
    }

    /**
     * Displays a showSimpleMessageStep to the user with a waiting symbol, and releases the script just afterward
     * @param textToShow a text that will be shown
     */
    public void wait(String textToShow) {
        messageSender.asynchronousSend(
                new InterrupterAsynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.get().showSpinnerStep((InterrupterAsynchroneousMessage) message, textToShow);
                    }
                }
        );
    }

    public ProgressControl progressBar(String textToShow) throws CancelException {
        UIMessageSender<ProgressControl> progressStepUIMessageSender = controller.createUIMessageSender();
        return progressStepUIMessageSender.synchronousSendAndGetResult(
                new InterrupterSynchronousMessage<ProgressControl>() {
                    @Override
                    public void execute(Message message) {
                        this.setResponse(setupWindow.get().showProgressBar((InterrupterSynchronousMessage) message,
                                textToShow));
                    }
                }
        );
    }

    public SetupWizard withLogContext(String logContextName) throws IOException {
        if(logContextName != null) {
            this.logContext = logStreamFactory.getLogger(logContextName);
        }
        return this;
    }

    public LogStream getLogContext() {
        return logContext;
    }

    public String getTitle() {
        return title;
    }
}
