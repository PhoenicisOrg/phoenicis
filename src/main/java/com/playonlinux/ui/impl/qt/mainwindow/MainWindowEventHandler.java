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

package com.playonlinux.ui.impl.qt.mainwindow;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.ui.api.UIEventHandler;
import com.playonlinux.ui.events.EventHandler;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QMainWindow;
import org.gnome.gtk.FileChooserAction;
import org.gnome.gtk.FileChooserDialog;

import java.io.File;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * EventDispatcher for the Qt-MainWindow.
 */
@Scan
public class MainWindowEventHandler implements UIEventHandler {
    @Inject
    static EventHandler mainEventHandler;

    private final QMainWindow parent;

    public MainWindowEventHandler(QMainWindow parent) {
        this.parent = parent;
    }


    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }


    /* MenuBar */

    public void runLocalScript() {
        QFileDialog fileDialog = new QFileDialog(parent);
        fileDialog.setViewMode(QFileDialog.ViewMode.List);
        fileDialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
        fileDialog.show();
    }

    public void exit() {
        QApplication.exit();
    }


}
