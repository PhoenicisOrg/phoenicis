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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDockWidget;
import com.trolltech.qt.gui.QWidget;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * Action SideBar of the MainWindow, containing all possible actions regarding the current context.
 */
public class MainWindowActionSideBar extends QDockWidget {

    public QWidget actionsSideBarContent;


    public MainWindowActionSideBar(MainWindow mainWindow){
        super(mainWindow);

        setupUi();
        retranslateUi();
    }

    private void setupUi(){
        setMinimumSize(new QSize(200, 40));
        setFeatures(QDockWidget.DockWidgetFeature.createQFlags(QDockWidget.DockWidgetFeature.DockWidgetFloatable, QDockWidget.DockWidgetFeature.DockWidgetMovable));
        setAllowedAreas(Qt.DockWidgetArea.createQFlags(Qt.DockWidgetArea.LeftDockWidgetArea, Qt.DockWidgetArea.RightDockWidgetArea));

        actionsSideBarContent = new QWidget();
        setWidget(actionsSideBarContent);
    }

    private void retranslateUi(){
        setWindowTitle(translate("Actions"));
    }

}
