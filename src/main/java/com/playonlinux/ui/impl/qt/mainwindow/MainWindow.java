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
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMessageBox;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * MainWindow of PlayOnLinux's Qt-Gui implementation.
 */
public class MainWindow extends QMainWindow implements PlayOnLinuxWindow {

    private UI_MainWindow ui;

    public MainWindow(){
        super();

        ui = new UI_MainWindow();
        ui.setupUi(this);

        this.show();
    }



    /* EVENTS */

    @Override
    protected void closeEvent(QCloseEvent e){
        QMessageBox confirmDialog = new QMessageBox();
        confirmDialog.setWindowTitle(translate("${application.name}"));
        confirmDialog.setText(translate("Are you sure you want to close all ${application.name} windows?"));
        confirmDialog.setIcon(QMessageBox.Icon.Question);
        confirmDialog.addButton(QMessageBox.StandardButton.Ok);
        confirmDialog.addButton(QMessageBox.StandardButton.Cancel);
        confirmDialog.setEscapeButton(QMessageBox.StandardButton.Cancel);
        confirmDialog.setDefaultButton(QMessageBox.StandardButton.Cancel);
        confirmDialog.exec();
        if(confirmDialog.clickedButton() == confirmDialog.escapeButton()){
            e.ignore();
        }
    }

}
