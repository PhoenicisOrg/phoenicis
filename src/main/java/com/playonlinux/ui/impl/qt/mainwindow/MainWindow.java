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

import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * MainWindow of PlayOnLinux's Qt-Gui implementation.
 */
public class MainWindow extends QMainWindow implements PlayOnLinuxWindow {

    private MainWindowMenuBar menuBar;
    private MainWindowToolBar toolBar;
    private MainWindowActionSideBar actionSideBar;

    private QWidget centralwidget;
    private QHBoxLayout mainLayout;

    private MainWindowShortcutList shortcutList;


    public MainWindow() {
        setupUi();
        retranslateUi();

        this.show();
    }

    private void setupUi(){
        menuBar = new MainWindowMenuBar(this);
        this.setMenuBar(menuBar);

        toolBar = new MainWindowToolBar(this);
        addToolBar(Qt.ToolBarArea.TopToolBarArea, toolBar);

        actionSideBar = new MainWindowActionSideBar(this);
        addDockWidget(Qt.DockWidgetArea.LeftDockWidgetArea, actionSideBar);

        centralwidget = new QWidget(this);
        setCentralWidget(centralwidget);
        mainLayout = new QHBoxLayout(centralwidget);
        mainLayout.setMargin(0);

        shortcutList = new MainWindowShortcutList(this);
        mainLayout.addWidget(shortcutList);

        resize(new QSize(800, 600).expandedTo(minimumSizeHint()));
    }

    private void retranslateUi(){
        setWindowTitle(translate("PlayOnLinux"));
    }



    /* EVENTS */

    @Override
    protected void closeEvent(QCloseEvent e) {
        QMessageBox confirmDialog = new QMessageBox();
        confirmDialog.setWindowTitle(translate("${application.name}"));
        confirmDialog.setText(translate("Are you sure you want to close all ${application.name} windows?"));
        confirmDialog.setIcon(QMessageBox.Icon.Question);
        confirmDialog.addButton(QMessageBox.StandardButton.Ok);
        confirmDialog.addButton(QMessageBox.StandardButton.Cancel);
        confirmDialog.setEscapeButton(QMessageBox.StandardButton.Cancel);
        confirmDialog.setDefaultButton(QMessageBox.StandardButton.Cancel);
        confirmDialog.exec();
        if (confirmDialog.clickedButton() == confirmDialog.escapeButton()) {
            e.ignore();
        }
    }

}
