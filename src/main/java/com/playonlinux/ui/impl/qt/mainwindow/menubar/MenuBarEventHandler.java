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

package com.playonlinux.ui.impl.qt.mainwindow.menubar;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.ui.api.UIEventHandler;
import com.playonlinux.ui.events.EventHandler;
import com.playonlinux.ui.impl.qt.mainwindow.MainWindow;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QMainWindow;

/**
 * EventHandler responsible for every interaction with the MenuBar.
 */
public class MenuBarEventHandler implements UIEventHandler {
    @Inject
    static EventHandler mainEventHandler;

    private final MainWindow parent;


    public MenuBarEventHandler(MainWindow parent){
        this.parent = parent;
    }

    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }



    public void runLocalScript() {
        QFileDialog fileDialog = new QFileDialog(parent);
        fileDialog.setViewMode(QFileDialog.ViewMode.List);
        fileDialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
        fileDialog.show();

        //TODO: pass through
    }

    public void exit() {
        parent.exit();
    }


}
