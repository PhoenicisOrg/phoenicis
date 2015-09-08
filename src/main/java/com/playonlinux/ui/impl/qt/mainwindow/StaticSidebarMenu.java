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

import com.playonlinux.ui.impl.qt.common.ResourceHelper;
import com.playonlinux.ui.impl.qt.mainwindow.sidebar.ActionMenu;
import com.playonlinux.ui.impl.qt.mainwindow.sidebar.ActionMenuItem;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * The static Menu always displayed in the ActionSideBar of the MainWindow.
 */
public class StaticSidebarMenu extends ActionMenu {

    private final MainWindow mainWindow;

    private ActionMenuItem installMenuItem;
    private ActionMenuItem runScriptMenuItem;


    public StaticSidebarMenu(MainWindow mainWindow) {
        super(mainWindow.getSideBar(), "PlayOnLinux");
        this.mainWindow = mainWindow;

        this.setupUi();
        this.connectSlots();
    }

    private void setupUi() {
        installMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/install.png"), translate("Install"));
        runScriptMenuItem = addMenuItem(ResourceHelper.getIcon("script/open.png"), translate("Run a script"));
    }

    private void connectSlots() {
        installMenuItem.clicked.connect(this, "installMenuItem_clicked()");
        runScriptMenuItem.clicked.connect(this, "runScriptMenuItem_clicked()");
    }


    private void installMenuItem_clicked() {
        //TODO: link to Install-Window
    }

    private void runScriptMenuItem_clicked() {
        mainWindow.getEventHandler().runLocalScript();
    }

}
