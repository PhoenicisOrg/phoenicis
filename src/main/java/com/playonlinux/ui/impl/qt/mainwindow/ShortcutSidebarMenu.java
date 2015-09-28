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
 * Context-aware Sidebar-Menu providing controls for the selected Shortcut
 */
public class ShortcutSidebarMenu extends ActionMenu {
    private final MainWindow mainWindow;

    private ActionMenuItem runMenuItem;
    private ActionMenuItem closeMenuItem;
    private ActionMenuItem debugMenuItem;
    private ActionMenuItem configureMenuItem;
    private ActionMenuItem createMenuItem;
    private ActionMenuItem openDirectoryMenuItem;
    private ActionMenuItem uninstallMenuItem;

    public ShortcutSidebarMenu(MainWindow mainWindow) {
        super(mainWindow.getSideBar(), "");
        this.mainWindow = mainWindow;

        setupUi();
        connectSlots();
    }

    private void setupUi() {
        runMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/run.png"), translate("Run"));
        closeMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/stop.png"), translate("Close"));
        debugMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/debug.png"), translate("Debug"));
        configureMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/configure.png"), translate("Configure"));
        createMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/create.png"), translate("Create Shortcut"));
        openDirectoryMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/openDir.png"), translate("Open directory"));
        uninstallMenuItem = addMenuItem(ResourceHelper.getIcon("shortcut/uninstall.png"), translate("Uninstall"));
    }

    private void connectSlots() {
        runMenuItem.clicked.connect(this, "runMenuItemClicked()");
        closeMenuItem.clicked.connect(this, "closeMenuItemClicked()");
        debugMenuItem.clicked.connect(this, "debugMenuItemClicked()");
        configureMenuItem.clicked.connect(this, "configureMenuItemClicked()");
        createMenuItem.clicked.connect(this, "createMenuItemClicked()");
        openDirectoryMenuItem.clicked.connect(this, "openDirectoryMenuItemClicked()");
        uninstallMenuItem.clicked.connect(this, "uninstallMenuItemClicked()");
    }


    /* EVENT HANDLERS */

    private void runMenuItemClicked() {
        //TODO: connect
    }

    private void closeMenuItemClicked() {
        //TODO: connect
    }

    private void debugMenuItemClicked() {
        //TODO: connect
    }

    private void configureMenuItemClicked() {
        //TODO: connect
    }

    private void createMenuItemClicked() {
        //TODO: connect
    }

    private void openDirectoryMenuItemClicked() {
        //TODO: connect
    }

    private void uninstallMenuItemClicked() {
        //TODO: connect
    }

}
