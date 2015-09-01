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

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.log.LoggerFactory;
import com.playonlinux.core.log.ScriptLogger;
import com.playonlinux.core.messages.*;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.framework.wizard.CompleteWizard;
import com.playonlinux.framework.wizard.SetupWizardComponent;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.ui.api.UIMessageSender;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.python.modules.Setup;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;

@Scan
public class SetupWizard implements CompleteWizard {
    private static final Logger LOGGER = Logger.getLogger(Setup.class);

    @Inject
    static Controller controller;

    @Inject
    static LoggerFactory loggerFactory;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private final String title;
    private UIMessageSender<String> messageSender;

    private SetupWindow setupWindow;
    private ScriptLogger logContext;

    private final List<WeakReference<SetupWizardComponent>> components;

    /**
     * Create the setupWindow
     *
     * @param title title of the setupWindow
     */
    public SetupWizard(String title) {
        this.title = title;
        this.components = new ArrayList<>();
    }

    @Override
    public void init() {
        this.messageSender = controller.createUIMessageSender();

        messageSender.synchronousSend(
                new SynchronousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow = controller.createSetupWindowGUIInstance(translate(title));
                    }
                }
        );
    }

    /**
     * Set the left image
     * @param leftImage URL of the left image
     */
    @Override
    public void setLeftImage(String leftImage) throws IOException {
        setupWindow.setLeftImage(new File(leftImage));
    }

   /**
    * Set the top image
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
        messageSender.synchronousSend(
                new SynchronousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.close();
                    }
                }
        );

        closeComponents();
    }


    /**
     * Shows a simple showSimpleMessageStep
     * @param textToShow the text to showRightView
     * @throws CancelException
     */
    @Override
    public void message(String textToShow) throws CancelException {
        messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showSimpleMessageStep((CancelerSynchronousMessage) message, textToShow);
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
    @Override
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
    @Override
    public void presentation(String textToShow) throws CancelException {
        messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showPresentationStep((CancelerSynchronousMessage) message, textToShow);
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
    @Override
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
    @Override
    public void licenceFile(String textToShow, String licenceFilePath) throws CancelException {
        licenceFile(textToShow, new File(licenceFilePath));
    }

    /**
     * Show a custom licence message
     * @param textToShow a message above the licence
     * @param licenceText the licence text to showRightView
     * @throws CancelException
     */
    @Override
    public String licence(String textToShow, String licenceText) throws CancelException {
        return messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showLicenceStep((CancelerSynchronousMessage) message, textToShow, licenceText);
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
    @Override
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
    @Override
    public String textbox(String textToShow, String defaultValue) throws CancelException {
        return messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showTextBoxStep((CancelerSynchronousMessage) message, textToShow, defaultValue);
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
    @Override
    public String menu(String textToShow, List<String> menuItems) throws CancelException {
        return messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showMenuStep((CancelerSynchronousMessage) message, textToShow, menuItems);
                    }
                }
        );
    }

    /**
     * Asks the user to choose a file a file
     * @param textToShow text to show
     * @return The path of the file
     * @throws CancelException
     */
    @Override
    public String browse(String textToShow) throws CancelException {
        return browse(textToShow, playOnLinuxContext.getUserHome(), null);
    }

    /**
     * Ask the user to choose a file
     * @param textToShow text to show
     * @param directory default directory to browse in
     * @param allowedExtensions A list containing allowed extensions. All extensions will be allowed if this parameter
     *                          is set to null
     * @return The path of the file
     * @throws CancelException
     */
    @Override
    public String browse(String textToShow, String directory, List<String> allowedExtensions) throws CancelException {
        return messageSender.synchronousSendAndGetResult(
                new CancelerSynchronousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showBrowseStep((CancelerSynchronousMessage) message, textToShow,
                                new File(directory), allowedExtensions);
                    }
                }
        );
    }


    /**
     * Displays a showSimpleMessageStep to the user with a waiting symbol, and releases the script just afterward
     * @param textToShow a text that will be shown
     */
    @Override
    public void wait(String textToShow) {
        messageSender.asynchronousSend(
                new InterrupterAsynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showSpinnerStep((InterrupterAsynchroneousMessage) message, textToShow);
                    }
                }
        );
    }

    @Override
    public ProgressControl progressBar(String textToShow) throws CancelException {
        UIMessageSender<ProgressControl> progressStepUIMessageSender = controller.createUIMessageSender();
        return progressStepUIMessageSender.synchronousSendAndGetResult(
                new InterrupterSynchronousMessage<ProgressControl>() {
                    @Override
                    public void execute(Message message) {
                        this.setResponse(setupWindow.showProgressBar((InterrupterSynchronousMessage) message,
                                textToShow));
                    }
                }
        );
    }

    @Override
    public ScriptLogger getLogContext() throws ScriptFailureException {
        if(logContext != null) {
            return logContext;
        } else {
            try {
                return loggerFactory.getScriptLogger(title);
            } catch (IOException e) {
                throw new ScriptFailureException("Unable to initalise log file", e);
            }
        }
    }

    @Override
    public void log(String message) throws ScriptFailureException {
        log(message, null);
    }

    @Override
    public void log(String message, Throwable e) throws ScriptFailureException {
        if(title != null) {
            OutputStream outputstream = getLogContext();
            PrintWriter printWriter = new PrintWriter(outputstream);
            printWriter.println(String.format("[%s] %s", Thread.currentThread().getName(), message));
            if(e != null) {
                printWriter.println(String.format("[%s] %s", Thread.currentThread().getName(), ExceptionUtils.getFullStackTrace(e)));
            }
            printWriter.flush();
        } else {
            LOGGER.warn("Unable to get the log context");
        }

        LOGGER.info(message);
        if(e != null) {
            LOGGER.info(ExceptionUtils.getFullStackTrace(e));
        }

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void registerComponent(SetupWizardComponent setupWizardComponent) {
        components.add(new WeakReference<>(setupWizardComponent));
    }

    private void closeComponents() {
        for(WeakReference<SetupWizardComponent> setupWizardComponentWeakReference: components) {
            SetupWizardComponent componentToClose = setupWizardComponentWeakReference.get();
            if(componentToClose != null) {
                componentToClose.close();
            }
        }
    }


}
