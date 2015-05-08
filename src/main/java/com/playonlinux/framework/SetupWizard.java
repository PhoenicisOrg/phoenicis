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

import com.playonlinux.api.Controller;
import com.playonlinux.api.ProgressStep;
import com.playonlinux.api.SetupWindow;
import com.playonlinux.api.UIMessageSender;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.domain.CancelException;
import com.playonlinux.utils.messages.*;

import java.util.List;

@Component
public class SetupWizard {

    @Inject
    static Controller controller;

    private final String title;

    SetupWindow setupWindow;
    UIMessageSender messageSender;



    /**
     * Create the setupWindow
     *
     * @param title title of the setupWindow
     */
    public SetupWizard(String title) {
        this.messageSender = controller.createUIMessageSender();
        this.title = title;

        messageSender.synchroneousSend(
                new SynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow = controller.createSetupWindowGUIInstance(title);
                    }
                }
        );
    }

    /**
     * Closes the setupWindow
     */
    public void close() {
        messageSender.synchroneousSend(
                new SynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.close();
                    }
                }
        );
    }

    /**
     * Shows a simple showSimpleMessageStep
     * @param textToShow the text to show
     * @throws InterruptedException
     * @throws CancelException
     */
    public void message(String textToShow) throws InterruptedException, CancelException {
        messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showSimpleMessageStep((CancelerSynchroneousMessage) message, textToShow);
                    }
                }
        );
    }

    public void presentation(String textToShow) throws CancelException, InterruptedException {
        messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showPresentationStep((CancelerSynchroneousMessage) message, textToShow);
                    }
                }
        );
    }
    /**
     * Ask the user to enter a value
     * @param textToShow a text that will be shown
     * @return the value the user entered
     * @throws InterruptedException
     * @throws CancelException
     */
    public String textbox(String textToShow) throws InterruptedException, CancelException {
        return this.textbox(textToShow, "");
    }

    /**
     * Asks the user to enter a value
     * @param textToShow a text that will be shown
     * @param defaultValue a default value
     * @return the value the user entered
     * @throws InterruptedException
     * @throws CancelException
     */
    public String textbox(String textToShow, String defaultValue) throws InterruptedException, CancelException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showTextBoxStep((CancelerSynchroneousMessage) message, textToShow, defaultValue);
                    }
                }
        );
    }

    /**
     * Displays a showMenuStep so that the user can make a choice
     * @param textToShow a text that will be shown
     * @param menuItems a list containing the elements of the showMenuStep
     * @return the value the user entered (as string)
     * @throws InterruptedException
     * @throws CancelException
     */
    public String menu(String textToShow, List<String> menuItems) throws InterruptedException, CancelException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showMenuStep((CancelerSynchroneousMessage) message, textToShow, menuItems);
                    }
                }
        );
    }

    /**
     * Displays a showSimpleMessageStep to the user with a waiting symbol, and releases the script just afterward
     * @param textToShow a text that will be shown
     */
    public void wait(String textToShow) {
        messageSender.asynchroneousSend(
                new InterrupterAsynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showSpinnerStep((InterrupterAsynchroneousMessage) message, textToShow);
                    }
                }
        );
    }

    public ProgressStep progressBar(String textToShow) throws CancelException, InterruptedException {
        return (ProgressStep) messageSender.synchroneousSendAndGetResult(
                new InterrupterSynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        this.setResponse(setupWindow.showProgressBar((InterrupterSynchroneousMessage) message,
                                textToShow));
                    }
                }
        );
    }


}
