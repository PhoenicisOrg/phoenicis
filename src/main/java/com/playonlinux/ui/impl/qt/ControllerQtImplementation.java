/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.ui.impl.qt;


import com.playonlinux.core.injection.Scan;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.ui.api.UIMessageSender;
import com.playonlinux.ui.impl.qt.mainwindow.MainWindow;
import com.trolltech.qt.gui.QApplication;

/**
 * Controller and entrypoint for POL's Qt-Gui implementation.
 */
@Scan
public class ControllerQtImplementation implements Controller {

    public static final String[] ARGS = new String[0];

    public void startApplication() {
        QApplication.initialize(ARGS);

        MainWindow w = new MainWindow();

        QApplication.execStatic();
        QApplication.shutdown();
    }

    //FIXME
    public SetupWindow createSetupWindowGUIInstance(String title) {
        return null;
    }

    @Override
    public <T> UIMessageSender<T> createUIMessageSender() {
        return new UIMessageSenderQtImplementation<>();
    }


}
