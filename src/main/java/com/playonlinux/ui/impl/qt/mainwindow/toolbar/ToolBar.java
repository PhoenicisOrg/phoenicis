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

package com.playonlinux.ui.impl.qt.mainwindow.toolbar;

import com.playonlinux.ui.impl.qt.common.ResourceHelper;
import com.playonlinux.ui.impl.qt.mainwindow.MainWindow;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolBar;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * ToolBar of the MainWindow
 */
public class ToolBar extends QToolBar {

    private MainWindow mainWindow;

    private QAction actionToolRun;
    private QAction actionToolClose;
    private QAction actionToolInstall;
    private QAction actionToolRemove;
    private QAction actionToolConfigure;


    public ToolBar(MainWindow mainWindow){
        super(mainWindow);
        this.mainWindow = mainWindow;

        setupUi();
        retranslateUi();
    }

    private void setupUi(){
        setIconSize(new QSize(32, 32));
        setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextUnderIcon);

        actionToolRun = new QAction(mainWindow);
        actionToolRun.setIcon(ResourceHelper.getIcon("shortcut/run.png"));
        actionToolClose = new QAction(mainWindow);
        actionToolClose.setIcon(ResourceHelper.getIcon("shortcut/stop.png"));
        actionToolInstall = new QAction(mainWindow);
        actionToolInstall.setIcon(ResourceHelper.getIcon("shortcut/install.png"));
        actionToolRemove = new QAction(mainWindow);
        actionToolRemove.setIcon(ResourceHelper.getIcon("shortcut/uninstall.png"));
        actionToolConfigure = new QAction(mainWindow);
        actionToolConfigure.setIcon(ResourceHelper.getIcon("shortcut/configure.png"));

        addAction(actionToolRun);
        addAction(actionToolClose);
        addSeparator();
        addAction(actionToolInstall);
        addAction(actionToolRemove);
        addSeparator();
        addAction(actionToolConfigure);
    }

    private void retranslateUi(){
        setWindowTitle(translate("toolBar"));

        actionToolRun.setText(translate("Run"));
        actionToolRun.setToolTip(translate("Run the currently selected shortcut"));
        actionToolClose.setText(translate("Close"));
        actionToolClose.setToolTip(translate("Close all instances of the currently selected shortcut"));
        actionToolInstall.setText(translate("Install"));
        actionToolInstall.setToolTip(translate("Install new software"));
        actionToolRemove.setText(translate("Remove"));
        actionToolRemove.setToolTip(translate("Delete the application that the currently selected shortcut belongs to"));
        actionToolConfigure.setText(translate("Configure"));
        actionToolConfigure.setToolTip(translate("Configure the currently selected shortcut"));
    }

}
