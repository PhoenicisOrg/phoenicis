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

package com.playonlinux.javafx;

import com.playonlinux.javafx.mainwindow.MainWindow;
import com.playonlinux.javafx.mainwindow.library.ViewLibrary;
import com.playonlinux.javafx.setupwindow.SetupWindowJavaFXImplementation;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.ui.api.UIMessageSender;

/**
 * JavaFX implementation of PlayOnLinux controller
 */
public class ControllerJavaFXImplementation implements Controller {
    @Override
    public void startApplication() {
        JavaFXApplication.launch(JavaFXApplication.class);
    }

    @Override
    public SetupWindow createSetupWindowGUIInstance(String title) {
        final MainWindow mainWindow = JavaFXApplication.getMainWindow();
        final ViewLibrary viewLibrary = mainWindow.getLibrary();
        final SetupWindowJavaFXImplementation setupWindowTab = new SetupWindowJavaFXImplementation(title, viewLibrary);
        viewLibrary.createNewTab(setupWindowTab);
        return setupWindowTab;
    }

    @Override
    public <T> UIMessageSender<T> createUIMessageSender() {
        return new UIMessageSenderJavaFXImplementation<>();
    }
}
